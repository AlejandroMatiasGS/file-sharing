using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace desktop_app.Modelo {
    public class MyFile {

        public MyFile(string path, long size, string name) {
            Path = path;
            Size = size;
            Name = name;
        }

        public string Path { get; set; }

        public long Size { get; set; }

        public string Name { get; set; }
    }
}
