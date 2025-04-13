import 'dart:io';

import 'package:file_sharing_mobile/models/interface.dart';
import 'package:file_sharing_mobile/views/widgets/interface.dart';
import 'package:flutter/material.dart';

class InterfaceSelector extends StatefulWidget {
  final String path;

  const InterfaceSelector({super.key, required this.path});

  @override
  State<InterfaceSelector> createState() => _InterfaceSelectorState();
}

class _InterfaceSelectorState extends State<InterfaceSelector> {
  List<MiInterface> interfaces = [];

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _init();
    });
  }

    _init() async {
      final inters = await NetworkInterface.list(
        includeLinkLocal: false,
        type: InternetAddressType.IPv4,
      );

      for (var i in inters) {
        setState(() {
          interfaces.add(MiInterface(name: i.name, ip: i.addresses.first.address));
        });
      }
    }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("File Sharing")),
      body: Padding(
        padding: EdgeInsets.only(left: 10, right: 10),
        child: Column(
          children: [
            const SizedBox(height: 30),
            const Text("Selecciona una interfaz", style: TextStyle(fontSize: 24)),
            const SizedBox(height: 20),
            Expanded(
              child: Container(
                decoration: BoxDecoration(
                  border: Border.all(color: Colors.black, width: 1),
                  borderRadius: BorderRadius.circular(10),
                ),
                child: ListView.builder(
                  itemCount: interfaces.length,
                  itemBuilder: (context, index) {
                    return Padding(
                      padding: const EdgeInsets.only(top: 10, left: 10, right: 10),
                      child: InterfaceWidget(interface: interfaces[index], path: widget.path),
                    );
                  },
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
