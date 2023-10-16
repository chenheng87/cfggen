using System;
using System.Collections.Generic;
using System.IO;

namespace Config.Ai
{
    public partial class DataAi
    {
        public int ID { get; private set; }
        public string Desc { get; private set; } /* ����----��������¶���Ч��--����һ��*/
        public string CondID { get; private set; } /* ������ʽ*/
        public int TrigTick { get; private set; } /* �������(֡)*/
        public int TrigOdds { get; private set; } /* ��������*/
        public string ActionID { get; private set; } /* ������Ϊ*/
        public bool DeathRemove { get; private set; } /* �����Ƴ�*/

        public override int GetHashCode()
        {
            return ID.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (obj == this) return true;
            var o = obj as DataAi;
            return o != null && ID.Equals(o.ID);
        }

        public override string ToString()
        {
            return "(" + ID + "," + Desc + "," + CondID + "," + TrigTick + "," + TrigOdds + "," + ActionID + "," + DeathRemove + ")";
        }

        static Config.KeyedList<int, DataAi> all = null;

        public static DataAi Get(int iD)
        {
            DataAi v;
            return all.TryGetValue(iD, out v) ? v : null;
        }

        public static List<DataAi> All()
        {
            return all.OrderedValues;
        }

        public static List<DataAi> Filter(Predicate<DataAi> predicate)
        {
            var r = new List<DataAi>();
            foreach (var e in all.OrderedValues)
            {
                if (predicate(e))
                    r.Add(e);
            }
            return r;
        }

        internal static void Initialize(Config.Stream os, Config.LoadErrors errors)
        {
            all = new Config.KeyedList<int, DataAi>();
            for (var c = os.ReadInt32(); c > 0; c--) {
                var self = _create(os);
                all.Add(self.ID, self);
            }
        }

        internal static DataAi _create(Config.Stream os)
        {
            var self = new DataAi();
            self.ID = os.ReadInt32();
            self.Desc = os.ReadString();
            self.CondID = os.ReadString();
            self.TrigTick = os.ReadInt32();
            self.TrigOdds = os.ReadInt32();
            self.ActionID = os.ReadString();
            self.DeathRemove = os.ReadBool();
            return self;
        }

    }
}
