
import 'dart:convert';
import 'dart:io';

import 'package:file_sharing_mobile/controllers/converter.dart';
import 'package:file_sharing_mobile/models/file.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class SendingProcess extends StatefulWidget {
  final String ip;
  final List<MiFile> files;

  const SendingProcess({super.key, required this.ip, required this.files});

  @override
  State<SendingProcess> createState() => _SendingProcessState();
}

class _SendingProcessState extends State<SendingProcess> {
  double progress = 0;
  bool stop = false;
  String? txtFile;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) { send(); });
  }

  Future<void> send() async {
    try {
      Socket s =  await Socket.connect(widget.ip, 7777);

      int totalSize = widget.files.map((f) => f.size).reduce((a, b) => a + b);

      Map<String, dynamic> jsonObj = {
        "totalSize": totalSize,
        "files": widget.files.map((f) => f.toJson()).toList(),
      };

      String json = jsonEncode(jsonObj);
      List<int> jsonBytes = utf8.encode(json);
      List<int> jsonSizeBytes = MiConverter.intToBytes(jsonBytes.length);

      s.add(jsonSizeBytes);
      s.add(jsonBytes);

      if(stop) return;

      int bytesSent = 0;
      for(MiFile f in widget.files) {
        setState(() { txtFile = "File: ${f.name}"; });
        
        final raf = await File(f.path).open(mode: FileMode.read);
        final buffer = Uint8List(1024);
        bool cont = true;
        while(cont && !stop) {
          final bytesRead = await raf.readInto(buffer);
          if(bytesRead > 0) {
            s.add(buffer);
            bytesSent += bytesRead;
            setState(() { progress = bytesSent / totalSize; });
          }else {
            cont = false;
          }
        }

        if(stop) break;
      }

      s.close();

      if(!mounted) return;
      final msgCtrl = ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Archivo(s) enviado(s)   con éxito"),
        duration: Duration(milliseconds: 1500),
      ));

      await msgCtrl.closed;
      if(!mounted) return;
      Navigator.popUntil(context, (route) => route.isFirst);
    }catch(e) {
      if(!mounted) return;
      final msgCtrl = ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Error en el proceso de envío \n$e"),
          duration: const Duration(milliseconds: 1500),
        ),
      );

      await msgCtrl.closed;
      if(!mounted) return;
      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: true,
      onPopInvokedWithResult: (didPop, result) {
        if(didPop) {
          setState(() { stop = true; });
        }
      },
      child: Scaffold(
        appBar: AppBar(
          title: const Text("File Sharing"),
          automaticallyImplyLeading: false,
        ),
        body: Padding(
          padding: EdgeInsets.all(10),
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Center(
                  child: Text(
                    "Progreso:",
                    style: TextStyle(fontSize: 30),
                    textAlign: TextAlign.center,
                  ),
                ),
                SizedBox(height: 20),
                LinearProgressIndicator(minHeight: 20, value: progress),
                SizedBox(height: 10),
                Text("File: $txtFile", style: TextStyle(fontSize: 24)),
                SizedBox(height: 30),
                SizedBox(
                  width: double.maxFinite,
                  child: TextButton(
                    onPressed: () { 
                      setState(() { stop = true; });
                      Navigator.pop(context);
                    },
                    style: TextButton.styleFrom(
                      backgroundColor: Colors.redAccent,
                      padding: const EdgeInsets.all(20),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.zero,
                      ),
                    ),
                    child: const Text(
                      "Cancelar",
                      style: TextStyle(color: Colors.white, fontSize: 24),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
