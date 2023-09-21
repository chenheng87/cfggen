using System;
using System.Collections.Generic;
using System.IO;

namespace Config.Other
{
    public partial class DataSignin
    {
        public int Id { get; private set; } /* ���ID*/
        public KeyedList<int, int> Item2countMap { get; private set; } /* ��ͨ����*/
        public KeyedList<int, int> Vipitem2vipcountMap { get; private set; } /* vip����*/
        public KeyedList<int, Config.Other.DataLoot> RefVipitem2vipcountMap { get; private set; }
        public int Viplevel { get; private set; } /* ��ȡvip��������͵ȼ�*/
        public string IconFile { get; private set; } /* ���ͼ��*/

        public override int GetHashCode()
        {
            return Id.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (obj == this) return true;
            var o = obj as DataSignin;
            return o != null && Id.Equals(o.Id);
        }

        public override string ToString()
        {
            return "(" + Id + "," + Item2countMap + "," + Vipitem2vipcountMap + "," + Viplevel + "," + IconFile + ")";
        }

        static Config.KeyedList<int, DataSignin> all = null;

        public static DataSignin Get(int id)
        {
            DataSignin v;
            return all.TryGetValue(id, out v) ? v : null;
        }

        class IdViplevelKey
        {
            readonly int Id;
            readonly int Viplevel;

            public IdViplevelKey(int id, int viplevel)
            {
                this.Id = id;
                this.Viplevel = viplevel;
            }

            public override int GetHashCode()
            {
                return Id.GetHashCode() + Viplevel.GetHashCode();
            }
            public override bool Equals(object obj)
            {
                if (obj == null) return false;
                if (obj == this) return true;
                var o = obj as IdViplevelKey;
                return o != null && Id.Equals(o.Id) && Viplevel.Equals(o.Viplevel);
            }
        }

        static Config.KeyedList<IdViplevelKey, DataSignin> idViplevelMap = null;

        public static DataSignin GetByIdViplevel(int id, int viplevel)
        {
            DataSignin v;
            return idViplevelMap.TryGetValue(new IdViplevelKey(id, viplevel), out v) ? v : null;
        }

        public static List<DataSignin> All()
        {
            return all.OrderedValues;
        }

        public static List<DataSignin> Filter(Predicate<DataSignin> predicate)
        {
            var r = new List<DataSignin>();
            foreach (var e in all.OrderedValues)
            {
                if (predicate(e))
                    r.Add(e);
            }
            return r;
        }

        internal static void Initialize(Config.Stream os, Config.LoadErrors errors)
        {
            all = new Config.KeyedList<int, DataSignin>();
            idViplevelMap = new Config.KeyedList<IdViplevelKey, DataSignin>();
            for (var c = os.ReadInt32(); c > 0; c--) {
                var self = _create(os);
                all.Add(self.Id, self);
                idViplevelMap.Add(new IdViplevelKey(self.Id, self.Viplevel), self);
            }
        }

        internal static void Resolve(Config.LoadErrors errors) {
            foreach (var v in All())
                v._resolve(errors);
        }

        internal static DataSignin _create(Config.Stream os)
        {
            var self = new DataSignin();
            self.Id = os.ReadInt32();
            self.Item2countMap = new KeyedList<int, int>();
            for (var c = os.ReadInt32(); c > 0; c--)
                self.Item2countMap.Add(os.ReadInt32(), os.ReadInt32());
            self.Vipitem2vipcountMap = new KeyedList<int, int>();
            for (var c = os.ReadInt32(); c > 0; c--)
                self.Vipitem2vipcountMap.Add(os.ReadInt32(), os.ReadInt32());
            self.Viplevel = os.ReadInt32();
            self.IconFile = os.ReadString();
            return self;
        }

        internal void _resolve(Config.LoadErrors errors)
        {
            RefVipitem2vipcountMap = new KeyedList<int, Config.Other.DataLoot>();
            foreach (var kv in Vipitem2vipcountMap.Map)
            {
                var k = kv.Key;
                var v = Config.Other.DataLoot.Get(kv.Value);
                if (v == null) errors.RefNull("other.signin", ToString(), "vipitem2vipcountMap", kv.Value);
                RefVipitem2vipcountMap.Add(k, v);
            }
	    }

    }
}
