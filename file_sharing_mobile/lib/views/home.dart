import 'package:file_picker/file_picker.dart';
import 'package:file_sharing_mobile/views/file_selector.dart';
import 'package:file_sharing_mobile/views/interface_selector.dart';
import 'package:flutter/material.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {

  void _selectPathAndContinue() async {
    String? selectedDirectory = await FilePicker.platform.getDirectoryPath();
  
    if (selectedDirectory != null) {
      if(!mounted) return;
        Navigator.push(context, MaterialPageRoute(builder: (context) => InterfaceSelector(path: selectedDirectory)));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("File Sharing")),
      body: Padding(
        padding: const EdgeInsets.all(50),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              SizedBox(
                width: double.maxFinite,
                child: TextButton(
                  onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => FileSelector())),
                  style: TextButton.styleFrom(
                    backgroundColor: Colors.indigo,
                    padding: const EdgeInsets.all(20),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.zero,
                    ),
                  ),
                  child: const Text("Enviar archivos", style: TextStyle(color: Colors.white, fontSize: 24)),
                ),
              ),
              SizedBox(height: 30),
              SizedBox(
                width: double.maxFinite,
                child: TextButton(
                  onPressed: () => _selectPathAndContinue(),
                  style: TextButton.styleFrom(
                    backgroundColor: Colors.indigo,
                    padding: const EdgeInsets.all(20),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.zero,
                    ),
                  ),
                  child: const Text("Recibir archivos", style: TextStyle(color: Colors.white, fontSize: 24)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
