package config.equip;

public class Jewelry {
    private int iD;
    private String name;
    private String iconFile;
    private config.LevelRank lvlRank;
    private String type;
    private int suitID;
    private int keyAbility;
    private int keyAbilityValue;
    private int salePrice;
    private String description;
    private config.equip.Jewelryrandom RefLvlRank;
    private config.equip.Jewelrytype RefType;
    private config.equip.Jewelrysuit NullableRefSuitID;
    private config.equip.Ability RefKeyAbility;

    private Jewelry() {
    }

    Jewelry(JewelryBuilder b) {
        this.iD = b.iD;
        this.name = b.name;
        this.iconFile = b.iconFile;
        this.lvlRank = b.lvlRank;
        this.type = b.type;
        this.suitID = b.suitID;
        this.keyAbility = b.keyAbility;
        this.keyAbilityValue = b.keyAbilityValue;
        this.salePrice = b.salePrice;
        this.description = b.description;
    }

    public static Jewelry _create(configgen.genjava.ConfigInput input) {
        Jewelry self = new Jewelry();
        self.iD = input.readInt();
        self.name = input.readStr();
        self.iconFile = input.readStr();
        self.lvlRank = config.LevelRank._create(input);
        self.type = input.readStr();
        self.suitID = input.readInt();
        self.keyAbility = input.readInt();
        self.keyAbilityValue = input.readInt();
        self.salePrice = input.readInt();
        self.description = input.readStr();
        return self;
    }

    /**
     * 首饰ID
     */
    public int getID() {
        return iD;
    }

    /**
     * 首饰名称
     */
    public String getName() {
        return name;
    }

    /**
     * 图标ID
     */
    public String getIconFile() {
        return iconFile;
    }

    /**
     * 首饰等级
     */
    public config.LevelRank getLvlRank() {
        return lvlRank;
    }

    /**
     * 首饰类型
     */
    public String getType() {
        return type;
    }

    /**
     * 套装ID（为0是没有不属于套装，首饰品级为4的首饰该参数为套装id，其余情况为0,引用JewelrySuit.csv）
     */
    public int getSuitID() {
        return suitID;
    }

    /**
     * 关键属性类型
     */
    public int getKeyAbility() {
        return keyAbility;
    }

    /**
     * 关键属性数值
     */
    public int getKeyAbilityValue() {
        return keyAbilityValue;
    }

    /**
     * 售卖价格
     */
    public int getSalePrice() {
        return salePrice;
    }

    /**
     * 描述,根据Lvl和Rank来随机3个属性，第一个属性由Lvl,Rank行随机，剩下2个由Lvl和小于Rank的行里随机。Rank最小的时候都从Lvl，Rank里随机。
     */
    public String getDescription() {
        return description;
    }

    public config.equip.Jewelryrandom refLvlRank() {
        return RefLvlRank;
    }

    public config.equip.Jewelrytype refType() {
        return RefType;
    }

    public config.equip.Jewelrysuit nullableRefSuitID() {
        return NullableRefSuitID;
    }

    public config.equip.Ability refKeyAbility() {
        return RefKeyAbility;
    }

    @Override
    public String toString() {
        return "(" + iD + "," + name + "," + iconFile + "," + lvlRank + "," + type + "," + suitID + "," + keyAbility + "," + keyAbilityValue + "," + salePrice + "," + description + ")";
    }

    public void _resolve(config.ConfigMgr mgr) {
        lvlRank._resolve(mgr);
        RefLvlRank = mgr.equip_jewelryrandom_All.get(lvlRank);
        java.util.Objects.requireNonNull(RefLvlRank);
        RefType = config.equip.Jewelrytype.get(type);
        java.util.Objects.requireNonNull(RefType);
        NullableRefSuitID = mgr.equip_jewelrysuit_All.get(suitID);
        RefKeyAbility = config.equip.Ability.get(keyAbility);
        java.util.Objects.requireNonNull(RefKeyAbility);
    }

    public static Jewelry get(int iD) {
        config.ConfigMgr mgr = config.ConfigMgr.getMgr();
        return mgr.equip_jewelry_All.get(iD);
    }

    public static java.util.Collection<Jewelry> all() {
        config.ConfigMgr mgr = config.ConfigMgr.getMgr();
        return mgr.equip_jewelry_All.values();
    }

    public static class _ConfigLoader implements config.ConfigLoader {

        @Override
        public void createAll(config.ConfigMgr mgr, configgen.genjava.ConfigInput input) {
            for (int c = input.readInt(); c > 0; c--) {
                Jewelry self = Jewelry._create(input);
                mgr.equip_jewelry_All.put(self.iD, self);
            }
        }

        @Override
        public void resolveAll(config.ConfigMgr mgr) {
            for (Jewelry e : mgr.equip_jewelry_All.values()) {
                e._resolve(mgr);
            }
        }

    }

}
