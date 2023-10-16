package configgen.genlua;

import configgen.gen.LangSwitch;
import configgen.value.TextI18n;
import configgen.value.TextI18n.TableI18n;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class LangSwitchSupport {
    private final LangSwitch langSwitch;
    private final List<String> defaultLangTexts;
    private final List<LangTexts> langTextsList;
    private int index = 0;


    private static class LangTexts {
        String lang;
        TextI18n langI18n;
        TableI18n curTableI18n;

        List<String> texts;
    }

    private static final int INIT_SIZE = 1024 * 32;

    LangSwitchSupport(LangSwitch langSwitch) {
        this.langSwitch = langSwitch;
        defaultLangTexts = new ArrayList<>(INIT_SIZE);
        defaultLangTexts.add("");  // 第一个是id为0，表示空字符串

        langTextsList = new ArrayList<>(langSwitch.lang2i18n().size());
        for (Map.Entry<String, TextI18n> e : langSwitch.lang2i18n().entrySet()) {
            List<String> texts = new ArrayList<>(INIT_SIZE);
            texts.add("");
            LangTexts lt = new LangTexts();
            lt.lang = e.getKey();
            lt.langI18n = e.getValue();
            lt.texts = texts;
            langTextsList.add(lt);
        }
    }

    void enterTable(String table) {
        for (LangTexts lt : langTextsList) {
            lt.curTableI18n = lt.langI18n.getTableI18n(table);
        }
    }

    int enterText(String original) {
        if (original.isEmpty()) {
            return 0;  // 空字符串
        }

        defaultLangTexts.add(original);
        for (LangTexts lt : langTextsList) {
            String text = null;
            if (lt.curTableI18n != null) {
                text = lt.curTableI18n.findText(original);
            }
            if (text == null) {
                text = original;
            }
            lt.texts.add(text);
        }
        index++;
        return index;
    }

    Map<String, List<String>> getLang2Texts() {
        Map<String, List<String>> lang2Texts = new LinkedHashMap<>();
        lang2Texts.put(langSwitch.defaultLang(), defaultLangTexts);
        for (LangTexts lt : langTextsList) {
            lang2Texts.put(lt.lang, lt.texts);
        }
        return lang2Texts;
    }
}
