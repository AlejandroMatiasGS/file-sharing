import 'package:file_sharing_mobile/models/interface.dart';
import 'package:file_sharing_mobile/views/receiving_process.dart';
import 'package:flutter/material.dart';

class InterfaceWidget extends StatelessWidget {
  final MiInterface interface;
  final String path;

  const InterfaceWidget({super.key, required this.interface, required this.path});
  
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        border: Border.all(color: Colors.black, width: 1),
        borderRadius: BorderRadius.circular(10),
      ),
      child: TextButton(
        onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => ReceivingProcess(interface: interface, path: path))),
        child: Padding(padding: EdgeInsets.only(left: 30, right: 30, top: 10, bottom: 10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(interface.name, style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.black, fontSize: 20,)),
              const SizedBox(height: 10),
              Text(interface.ip, style: const TextStyle(color: Colors.black, fontSize: 20)),
            ],
          ),
        ),
      ),
    );
  }
}