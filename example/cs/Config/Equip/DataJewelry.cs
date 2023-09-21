using System;
using System.Collections.Generic;
using System.IO;

namespace Config.Equip
{
    public partial class DataJewelry
    {
        public int ID { get; private set; } /* ����ID*/
        public string Name { get; private set; } /* ��������*/
        public string IconFile { get; private set; } /* ͼ��ID*/
        public Config.DataLevelrank LvlRank { get; private set; } /* ���εȼ�*/
        public Config.Equip.DataJewelryrandom RefLvlRank { get; private set; }
        public string Type { get; private set; } /* ��������*/
        public Config.Equip.DataJewelrytype RefType { get; private set; }
        public int SuitID { get; private set; } /* ��װID��Ϊ0��û�в�������װ������Ʒ��Ϊ4�����θò���Ϊ��װid���������Ϊ0,����JewelrySuit.csv��*/
        public Config.Equip.DataJewelrysuit NullableRefSuitID { get; private set; }
        public int KeyAbility { get; private set; } /* �ؼ���������*/
        public Config.Equip.DataAbility RefKeyAbility { get; private set; }
        public int KeyAbilityValue { get; private set; } /* �ؼ�������ֵ*/
        public int SalePrice { get; private set; } /* �����۸�*/
        public string Description { get; private set; } /* ����,����Lvl��Rank�����3�����ԣ���һ��������Lvl,Rank�������ʣ��2����Lvl��С��Rank�����������Rank��С��ʱ�򶼴�Lvl��Rank�������*/

        public override int GetHashCode()
        {
            return ID.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (obj == this) return true;
            var o = obj as DataJewelry;
            return o != null && ID.Equals(o.ID);
        }

        public override string ToString()
        {
            return "(" + ID + "," + Name + "," + IconFile + "," + LvlRank + "," + Type + "," + SuitID + "," + KeyAbility + "," + KeyAbilityValue + "," + SalePrice + "," + Description + ")";
        }

        static Config.KeyedList<int, DataJewelry> all = null;

        public static DataJewelry Get(int iD)
        {
            DataJewelry v;
            return all.TryGetValue(iD, out v) ? v : null;
        }

        public static List<DataJewelry> All()
        {
            return all.OrderedValues;
        }

        public static List<DataJewelry> Filter(Predicate<DataJewelry> predicate)
        {
            var r = new List<DataJewelry>();
            foreach (var e in all.OrderedValues)
            {
                if (predicate(e))
                    r.Add(e);
            }
            return r;
        }

        internal static void Initialize(Config.Stream os, Config.LoadErrors errors)
        {
            all = new Config.KeyedList<int, DataJewelry>();
            for (var c = os.ReadInt32(); c > 0; c--) {
                var self = _create(os);
                all.Add(self.ID, self);
            }
        }

        internal static void Resolve(Config.LoadErrors errors) {
            foreach (var v in All())
                v._resolve(errors);
        }

        internal static DataJewelry _create(Config.Stream os)
        {
            var self = new DataJewelry();
            self.ID = os.ReadInt32();
            self.Name = os.ReadString();
            self.IconFile = os.ReadString();
            self.LvlRank = Config.DataLevelrank._create(os);
            self.Type = os.ReadString();
            self.SuitID = os.ReadInt32();
            self.KeyAbility = os.ReadInt32();
            self.KeyAbilityValue = os.ReadInt32();
            self.SalePrice = os.ReadInt32();
            self.Description = os.ReadString();
            return self;
        }

        internal void _resolve(Config.LoadErrors errors)
        {
            LvlRank._resolve(errors);
            RefLvlRank = Config.Equip.DataJewelryrandom.Get(LvlRank);
            if (RefLvlRank == null) errors.RefNull("equip.jewelry", ToString(), "LvlRank", LvlRank);
            RefType = Config.Equip.DataJewelrytype.Get(Type);
            if (RefType == null) errors.RefNull("equip.jewelry", ToString(), "Type", Type);
            NullableRefSuitID = Config.Equip.DataJewelrysuit.Get(SuitID);
            RefKeyAbility = Config.Equip.DataAbility.Get(KeyAbility);
            if (RefKeyAbility == null) errors.RefNull("equip.jewelry", ToString(), "KeyAbility", KeyAbility);
	    }

    }
}
