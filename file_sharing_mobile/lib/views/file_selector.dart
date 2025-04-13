import 'package:file_picker/file_picker.dart';
import 'package:file_sharing_mobile/models/file.dart';
import 'package:file_sharing_mobile/views/sending_process.dart';
import 'package:file_sharing_mobile/views/widgets/file.dart';
import 'package:flutter/material.dart';

class FileSelector extends StatefulWidget {
  const FileSelector({super.key});

  @override
  State<FileSelector> createState() => _FileSelectorState();
}

class _FileSelectorState extends State<FileSelector> {
  List<MiFile> files = [];

  void addFiles() async {
    FilePickerResult? result = await FilePicker.platform.pickFiles(allowMultiple: true);

    if(result != null) {
      List<PlatformFile> files_ = result.files;
      setState(() {
        files = [...files, ...files_.map((file) => MiFile(name: file.name, path: file.path!, size: file.size))];
      });
    }
  }

  void removeFile(int index) { setState(() { files.removeAt(index); }); }

  void next() async {
    if(files.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("No hay archivos seleccionados"),
        duration: Duration(milliseconds: 1500),
      ));
    }else {
      TextEditingController ipTxtCtrl = TextEditingController();

      String? ip = await showDialog(
        context: context, 
        builder: (context) {
          return AlertDialog(
            title: Text('Ingresa la dirección ip de la app de recibo'),
            content: TextField(
              controller: ipTxtCtrl,
              decoration: InputDecoration(hintText: 'Escribe aquí'),
            ),
            actions: [
              TextButton(
                onPressed: () { Navigator.of(context).pop(); },
                child: Text('Cancelar'),
              ),
              TextButton(
                onPressed: () {
                  RegExp ipFormat = RegExp(r'^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$');
                  if(ipFormat.hasMatch(ipTxtCtrl.text)) {
                    Navigator.of(context).pop(ipTxtCtrl.text);
                  }else {
                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                      content: Text("Formato de IP inválido"),
                      duration: Duration(milliseconds: 1500),
                    ));
                  }
                },
                child: Text('Aceptar'),
              ),
            ],
          );
        }
      );

      if(ip == null) return;

      if(!mounted) return;  
      Navigator.push(context, MaterialPageRoute(builder: (context) => SendingProcess(ip: ip, files: files)));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("File Sharing")),
      body: Padding(
        padding: const EdgeInsets.only(right: 20, left: 20, top: 50, bottom: 50),
        child: Column(
          children: [
            SizedBox(
              width: double.maxFinite,
              child: TextButton(
                onPressed: addFiles,
                style: TextButton.styleFrom(
                  backgroundColor: Colors.indigo,
                  padding: const EdgeInsets.all(20),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.zero,
                  ),
                ),
                child: const Text("Agregar archivos...", style: TextStyle(color: Colors.white, fontSize: 24)),
              ),
            ),
            SizedBox(height: 30),
            Expanded(
              child: Container(
                decoration: BoxDecoration(
                  border: Border.all( color: Colors.black),
                  borderRadius: BorderRadius.circular(10),
                ),
                child: ListView.builder(
                  itemCount: files.length,
                  itemBuilder: (context, index) {
                    MiFile file = files[index];
                    return Padding(
                      padding: const EdgeInsets.only(top: 10),
                      child: FileWidget(index: index, onDelete: removeFile, file: file),
                    );  
                  },
                ),
              ),
            ),
            SizedBox(height: 30),
            SizedBox(
              width: double.maxFinite,
              child: TextButton(
                onPressed: next,
                style: TextButton.styleFrom(
                  backgroundColor: Colors.indigo,
                  padding: const EdgeInsets.all(20),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.zero,
                  ),
                ),
                child: const Text("Siguiente", style: TextStyle(color: Colors.white, fontSize: 24)),
              ),
            ),
          ]
        ),
      ),
    );
  }
}
