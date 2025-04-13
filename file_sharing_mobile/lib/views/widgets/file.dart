import 'package:file_sharing_mobile/models/file.dart';
import 'package:flutter/material.dart';

class FileWidget extends StatelessWidget{
  final int index;
  final MiFile file;
  final Function(int) onDelete;

  const FileWidget({super.key, required this.index, required this.onDelete, required this.file});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(left: 10, right: 10),
      child: Container(
        decoration: BoxDecoration(
          border: Border.all( color: Colors.black),
          borderRadius: BorderRadius.circular(10),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 16, right: 16, top: 8, bottom: 8)  ,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(file.name, style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.black, fontSize: 20,)),
                  const SizedBox(height: 5),
                  Text("${file.size} bytes", style: const TextStyle(color: Colors.black, fontSize: 20)),
                ],
              ),
            ),  
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: IconButton(
                icon: const Icon(Icons.delete, color: Colors.red, size: 50,),
                onPressed: () => onDelete(index),
              ),
            ),
          ],
        ),
      ),
    );
  }
}