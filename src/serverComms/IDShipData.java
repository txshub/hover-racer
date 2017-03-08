package serverComms;

import physics.network.ShipSetupData;
import physics.placeholders.DataGenerator;

public class IDShipData {

  final int id;
  final ShipSetupData data;

  public IDShipData(int id, ShipSetupData data) {
    this.id = id;
    this.data = data;
  }

  public IDShipData(String in) {
    String collected = "";
    while (in.charAt(0) != '|') {
      collected += in.charAt(0);
      in = in.substring(1);
    }
    id = Integer.parseInt(collected);
    in = in.substring(1);
    data = DataGenerator.fromJson(in);

  }

  public String toString() {
    return id + "|" + data.toString();
  }

  public byte[] toByteArray() {
    return toString().getBytes(ServerComm.charset);
  }

}
