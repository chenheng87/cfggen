package config

type OtherMonster struct {
    id int32
    posList []*Position
}

func createOtherMonster(stream *Stream) *OtherMonster {
    v := &OtherMonster{}
    v.id = stream.ReadInt32()
    posListSize := stream.ReadInt32()
    v.posList = make([]*Position, posListSize)
    for i := 0; i < int(posListSize); i++ {
        v.posList[i] = createPosition(stream)
    }
    return v
}

//getters
func (t *OtherMonster) Id() int32 {
    return t.id
}

func (t *OtherMonster) PosList() []*Position {
    return t.posList
}

type OtherMonsterMgr struct {
    all []*OtherMonster
    idMap map[int32]*OtherMonster
}

func(t *OtherMonsterMgr) GetAll() []*OtherMonster {
    return t.all
}

func(t *OtherMonsterMgr) Get(id int32) *OtherMonster {
    return t.idMap[id]
}

func (t *OtherMonsterMgr) Init(stream *Stream) {
    cnt := stream.ReadInt32()
    t.all = make([]*OtherMonster, 0, cnt)
    t.idMap = make(map[int32]*OtherMonster, cnt)
    for i := 0; i < int(cnt); i++ {
        v := createOtherMonster(stream)
        t.all = append(t.all, v)
        t.idMap[v.id] = v
    }
}
