package config.task;

public enum Completeconditiontype {
    KILLMONSTER("KillMonster", 1),
    TALKNPC("TalkNpc", 2),
    COLLECTITEM("CollectItem", 3),
    CONDITIONAND("ConditionAnd", 4),
    CHAT("Chat", 5),
    TESTNOCOLUMN("TestNoColumn", 6);

    private final String name;
    private final int value;

    Completeconditiontype(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private static final java.util.Map<Integer, Completeconditiontype> map = new java.util.HashMap<>();

    static {
        for(Completeconditiontype e : Completeconditiontype.values()) {
            map.put(e.value, e);
        }
    }

    public static Completeconditiontype get(int value) {
        return map.get(value);
    }

    /**
     * 任务完成条件类型（id的范围为1-100）
     */
    public int getId() {
        return value;
    }

    /**
     * 程序用名字
     */
    public String getName() {
        return name;
    }

}
