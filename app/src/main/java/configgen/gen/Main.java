package configgen.gen;

import configgen.genjava.BinaryToText;
import configgen.genjava.GenJavaData;
import configgen.genjava.code.GenJavaCode;
import configgen.tool.XmlToCfg;
import configgen.util.Logger;
import configgen.tool.ValueSearcher;
import configgen.util.CachedFiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class Main {
    private static void usage(String reason) {
        System.out.println(reason);

        System.out.println("Usage: app.bat -datadir [dir] [options] [gens]");
        System.out.println();
        System.out.println("----配置表信息--------------------------------------");
        System.out.println("    -datadir      配表根目录，根目录可以有个config.xml");
        System.out.println("    -encoding     csv编码，默认是GBK，如果文件中含有bom则用bom标记的编码");
        System.out.println("    -verify       检查配表约束");

        System.out.println();
        System.out.println("----国际化支持--------------------------------------");
        System.out.println("    -i18nfile     国际化需要的文件，如果不用国际化，就不要配置");
        System.out.println("    -i18nencoding 国际化需要的文件的编码，默认是GBK，如果文件中含有bom则用bom标记的编码");
        System.out.println("    -i18ncrlfaslf 把字符串里的\\r\\n 替换为 \\n，默认是false");
        System.out.println("    -langSwitchDir 国际化并且可随时切换语言");

        System.out.println();
        System.out.println("----小工具--------------------------------------");
        System.out.println("    -binaryToText       后可接1或2个参数（java data的file，table名称-用startsWith匹配），打印table的定义和数据");
        System.out.println("    -binaryToTextLoop   后可接1个参数（java data的file），打印table的定义和数据");
        System.out.println("    -search             后接命令，找到匹配的数据");

        System.out.println("    -dump         打印内部树结构");
        System.out.println("    -v[1]         输出一些额外信息,1是额外gc测试内存");

        System.out.println();
        System.out.println("----以下gen参数之间由,分割,参数名和参数取值之间由=或:分割--------------------------------------");
        Generators.getAllProviders().forEach((k, v) -> {
                    System.out.printf("    -gen %s\n", k);
                    Usage usage = new Usage();
                    v.create(usage);
                    usage.print();
                }
        );


        Runtime.getRuntime().exit(1);
    }

    public static void main(String[] args) throws Exception {
        try {
            main0(args);
        } catch (Throwable t) {
            String newLine = System.lineSeparator();
            StringBuilder sb = new StringBuilder();
            sb.append("-------------------------错误描述-------------------------").append(newLine);
            int stackCnt = 0;
            Throwable curr = t;
            while (curr != null && ++stackCnt < 10) {
                sb.append(curr.getMessage()).append(newLine);
                curr = curr.getCause();
            }
            sb.append("-------------------------错误堆栈-------------------------").append(newLine);
            System.out.print(sb);

            throw t;
        }
    }

    private static void main0(String[] args) throws Exception {
        Generators.addProvider("java", GenJavaCode::new);
        Generators.addProvider("javadata", GenJavaData::new);
//
//        Generators.addProvider("lua", GenLua::new);
//        Generators.addProvider("cs", GenCs::new);
//        Generators.addProvider("pack", GenPack::new);
//        Generators.addProvider("bytes", GenBytes::new);
//
//        Generators.addProvider("i18n", GenI18n::new);
//        Generators.addProvider("allrefvalues", GenAllRefValues::new);


        String datadir = null;
        boolean xmlToCfg = false;
        int headRow = 2;
        String encoding = "GBK";

        String i18nfile = null;
        String i18nencoding = "GBK";
        boolean i18ncrlfaslf = false;
        String langSwitchDir = null;

        boolean verify = false;
        List<Generator> generators = new ArrayList<>();


        boolean binaryToTextLoop = false;
        String binaryToTextFile = null;
        String match = null;


        List<String> searchParam = null;

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-datadir":
                    datadir = args[++i];
                    break;
                case "-xmltocfg":
                    xmlToCfg = true;
                    break;
                case "-headrow":
                    headRow = Integer.parseInt(args[++i]);
                    break;

                case "-encoding":
                    encoding = args[++i];
                    break;
                case "-verify":
                    verify = true;
                    break;

                case "-i18nfile":
                    i18nfile = args[++i];
                    break;
                case "-i18nencoding":
                    i18nencoding = args[++i];
                    break;
                case "-i18ncrlfaslf":
                    i18ncrlfaslf = true;
                    break;
                case "-langSwitchDir":
                    langSwitchDir = args[++i];
                    break;

                case "-v":
                    Logger.setVerboseLevel(1);
                    break;
                case "-v1":
                    Logger.setVerboseLevel(1);
                    Logger.enableProfileGc();
                    break;

                case "-binaryToText":
                    binaryToTextFile = args[++i];
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        match = args[++i];
                    }
                    break;
                case "-binaryToTextLoop":
                    binaryToTextLoop = true;
                    binaryToTextFile = args[++i];
                    break;

                case "-search":
                    searchParam = new ArrayList<>();
                    while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        searchParam.add(args[++i]);
                    }
                    break;

                case "-gen":
                    Generator generator = Generators.create(args[++i]);
                    if (generator == null)
                        usage("");
                    generators.add(generator);
                    break;


                default:
                    usage("unknown args " + args[i]);
                    break;
            }
        }

        if (binaryToTextFile != null) {
            if (binaryToTextLoop) {
                BinaryToText.loop(binaryToTextFile);
            } else {
                BinaryToText.parse(binaryToTextFile, match);
            }
            return;
        }
        if (datadir == null) {
            usage("请需要配置-datadir");
            return;
        }

        Path dataDir = Paths.get(datadir);
        Logger.setVerboseLevel(0);
        Logger.enableProfile();

        if (xmlToCfg) {
            XmlToCfg.convertAndCheck(dataDir);
            return;
        }

        if (i18nfile != null && langSwitchDir != null) {
            usage("-不能同时配置-i18nfile和-langSwitchDir");
            return;
        }

        Logger.profile(String.format("start total memory %dm", Runtime.getRuntime().maxMemory() / 1024 / 1024));
        Context ctx = new Context(dataDir, headRow, encoding);
        ctx.setI18nOrLangSwitch(i18nfile, langSwitchDir, i18nencoding, i18ncrlfaslf);

        if (searchParam != null) {
            ValueSearcher searcher = new ValueSearcher(ctx.makeValue());
            if (searchParam.isEmpty()) {
                searcher.loop();
            } else {
                searcher.search(searchParam.get(0), searchParam.subList(1, searchParam.size()));
            }
            return;
        }

        if (verify) {
            Logger.verbose("-----start verify");
            ctx.makeValue();
        }

        for (Generator generator : generators) {
            Logger.verbose("-----generate " + generator.parameter);
            generator.generate(ctx);
        }

        CachedFiles.finalExit();
        Logger.profile("end");
    }


}
