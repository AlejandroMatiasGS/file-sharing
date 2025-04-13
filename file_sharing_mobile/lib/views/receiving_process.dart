import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';
import 'package:file_sharing_mobile/controllers/converter.dart';
import 'package:path/path.dart' as path;

import 'package:file_sharing_mobile/models/interface.dart';
import 'package:flutter/material.dart';

class ReceivingProcess extends StatefulWidget {
  final MiInterface interface;
  final String path;

  const ReceivingProcess({
    super.key,
    required this.interface,
    required this.path,
  });

  @override
  State<ReceivingProcess> createState() => _ReceivingProcessState();
}

class _ReceivingProcessState extends State<ReceivingProcess> {
  double progress = 0;
  bool stop = false;
  String? txtFile;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      receive();
    });
  }

  void receive() async {
    Socket? s;
    ServerSocket? ss;

    try {
      ss = await ServerSocket.bind(InternetAddress.anyIPv4, 7777);

      while (!stop) {
        try {
          s = await ss.timeout(const Duration(seconds: 5)).first;
          break;  
          // ignore: empty_catches
        } on TimeoutException {}
      }

      if (stop) return;

      final buffer = <int>[];
      int fase = 0;

      int jsonLength = 0;
      List<int> jsonBytes = [];
      Map<String, dynamic> jsonMap = {};
      List files = [];
      int fileIndex = 0;
      int remainingFileBytes = 0;
      List<int> fileBytes = [];
      int bytesReceived = 0;
      late StreamSubscription subs;

      subs = s!.listen(
        (List<int> data) {
          buffer.addAll(data);

          while (true) {
            if (fase == 0 && buffer.length >= 4 && !stop) {
              final header = Uint8List.fromList(buffer.sublist(0, 4));
              jsonLength = MiConverter.bytesToInt(header);
              buffer.removeRange(0, 4);
              fase = 1;
            } else if (fase == 1 && buffer.length >= jsonLength && !stop) {
              jsonBytes = buffer.sublist(0, jsonLength);
              final jsonStr = utf8.decode(jsonBytes);
              jsonMap = jsonDecode(jsonStr);
              files = jsonMap['files'];
              buffer.removeRange(0, jsonLength);
              fase = 2;
              remainingFileBytes = files[fileIndex]['size'];
              fileBytes = [];
              setState(() {
                txtFile = "File: ${files[fileIndex]['name']}";
              });
            } else if (fase == 2 && buffer.isNotEmpty && !stop) {
              final chunkSize =
                  remainingFileBytes < buffer.length
                      ? remainingFileBytes
                      : buffer.length;

              fileBytes.addAll(buffer.sublist(0, chunkSize));
              bytesReceived += chunkSize;
              setState(() {
                progress = bytesReceived / jsonMap['totalSize'];
              });
              buffer.removeRange(0, chunkSize);
              remainingFileBytes -= chunkSize;

              if (remainingFileBytes == 0) {
                final nombre = files[fileIndex]['name'];
                File(
                  path.join(widget.path, nombre),
                ).writeAsBytesSync(fileBytes);

                fileIndex++;
                setState(() {
                  txtFile = "File: ${files[fileIndex]['name']}";
                });

                if (fileIndex >= files.length) {
                  s!.destroy();
                  return;
                } else {
                  remainingFileBytes = files[fileIndex]['size'];
                  fileBytes = [];
                }
              }
            } else {
              break;
            }
          }

          if (stop) {
            subs.cancel();
            s?.destroy();
            ss?.close();
            Navigator.pop(context);
            return;
          }
        },
        onDone: () {
          s?.destroy();
          ss?.close();
          var msgCtrl = ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text("Archivo(s) recibido(s) con éxito"),
              duration: Duration(milliseconds: 1500),
            ),
          );

          msgCtrl.closed.then((value) {
            if (!mounted) return;
            Navigator.popUntil(context, (route) => route.isFirst);
          });
        },
        onError: (err) {
          s?.destroy();
          ss?.close();
          var msgCtrl = ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text("Error en el proceso de recepción"),
              duration: const Duration(milliseconds: 1500),
            ),
          );

          msgCtrl.closed.then((value) {
            if (!mounted) return;
            Navigator.pop(context);
          });
        },
      );
    } catch (e) {
      s?.destroy();
      ss?.close();
      var msgCtrl = ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Error en el proceso de recepción."),
          duration: const Duration(milliseconds: 1500),
        ),
      );

      msgCtrl.closed.then((value) {
        if (!mounted) return;
        Navigator.pop(context);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: true,
      onPopInvokedWithResult: (didPop, result) {
        if (didPop) {
          setState(() {
            stop = true;
          });
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
                    "Ingresa esta IP en la app de envío",
                    style: TextStyle(fontSize: 24),
                  ),
                ),
                SizedBox(height: 10),
                Center(
                  child: Text(
                    widget.interface.ip,
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                ),
                SizedBox(height: 40),
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
                      setState(() {
                        stop = true;
                      });
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
