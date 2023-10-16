using System;
using System.Collections.Generic;
using System.IO;

namespace Config.Task
{
    public partial class DataTask
    {
        public int Taskid { get; private set; } /* ��������������ͣ�id�ķ�ΧΪ1-100��*/
        public List<string> Name { get; private set; } /* ����������*/
        public int Nexttask { get; private set; }
        public Config.Task.DataCompletecondition Completecondition { get; private set; }
        public int Exp { get; private set; }
        public Config.Task.DataTestdefaultbean TestDefaultBean { get; private set; } /* ����*/
        public Config.Task.DataTaskextraexp NullableRefTaskid { get; private set; }
        public Config.Task.DataTask NullableRefNexttask { get; private set; }

        public override int GetHashCode()
        {
            return Taskid.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (obj == this) return true;
            var o = obj as DataTask;
            return o != null && Taskid.Equals(o.Taskid);
        }

        public override string ToString()
        {
            return "(" + Taskid + "," + CSV.ToString(Name) + "," + Nexttask + "," + Completecondition + "," + Exp + "," + TestDefaultBean + ")";
        }

        static Config.KeyedList<int, DataTask> all = null;

        public static DataTask Get(int taskid)
        {
            DataTask v;
            return all.TryGetValue(taskid, out v) ? v : null;
        }

        public static List<DataTask> All()
        {
            return all.OrderedValues;
        }

        public static List<DataTask> Filter(Predicate<DataTask> predicate)
        {
            var r = new List<DataTask>();
            foreach (var e in all.OrderedValues)
            {
                if (predicate(e))
                    r.Add(e);
            }
            return r;
        }

        internal static void Initialize(Config.Stream os, Config.LoadErrors errors)
        {
            all = new Config.KeyedList<int, DataTask>();
            for (var c = os.ReadInt32(); c > 0; c--) {
                var self = _create(os);
                all.Add(self.Taskid, self);
            }
        }

        internal static void Resolve(Config.LoadErrors errors) {
            foreach (var v in All())
                v._resolve(errors);
        }

        internal static DataTask _create(Config.Stream os)
        {
            var self = new DataTask();
            self.Taskid = os.ReadInt32();
            self.Name = new List<string>();
            for (var c = os.ReadInt32(); c > 0; c--)
                self.Name.Add(os.ReadString());
            self.Nexttask = os.ReadInt32();
            self.Completecondition = Config.Task.DataCompletecondition._create(os);
            self.Exp = os.ReadInt32();
            self.TestDefaultBean = Config.Task.DataTestdefaultbean._create(os);
            return self;
        }

        internal void _resolve(Config.LoadErrors errors)
        {
            Completecondition._resolve(errors);
            NullableRefTaskid = Config.Task.DataTaskextraexp.Get(Taskid);
            NullableRefNexttask = Config.Task.DataTask.Get(Nexttask);
	    }

    }
}
