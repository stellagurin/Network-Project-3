import java.io.*;
import java.util.*;

 public class Traceroute {

	public static void main(String[] args) {

		String tracerouteFile = "./sampletcpdump.txt";
		String pFile = "./output.txt";
		String inputLine;
		int curPos = 0;
		int ttlNumCount = 0;
		String id;
		String TTL;
		String messageTime;
		String responseTime;
		double differenceTime;

		try {
			BufferedReader bufferReader = new BufferedReader(new FileReader(tracerouteFile));
			FileWriter fileWriter = new FileWriter(pFile);

			ArrayList<String> traceList = new ArrayList<String>();

			while((inputLine =bufferReader.readLine()) != null) {
				traceList.add(inputLine);
			}

			for(String s: traceList) {
				String cLine = traceList.get(curPos);

				if(cLine.indexOf("id") >= 0) {

					id = cLine.substring(cLine.indexOf("id"), cLine.indexOf(", offset"));
					TTL = cLine.substring(cLine.indexOf("ttl") + 3, cLine.indexOf(", id"));
					messageTime = cLine.substring(0, cLine.indexOf("IP"));

				for(int i = curPos + 1; i < traceList.size(); i++) {
					String response = traceList.get(i);

					if(response.indexOf("id") >= 0) {
						String responseID = response.substring(response.indexOf("id"), response.indexOf(", offset"));

						if (responseID.equals(id) && !responseID.equals("id 0")) {
							String responseIP = traceList.get(i-1);
							responseIP = responseIP.substring(4, responseIP.indexOf(">"));

							responseTime = traceList.get(i-2);
							responseTime = responseTime.substring(0, responseTime.indexOf("IP"));

							differenceTime = (Double.parseDouble(responseTime) - Double.parseDouble(messageTime)) * 1000;

							differenceTime = Math.round(differenceTime * 1000d) / 1000d;

							if(ttlNumCount % 3 == 0) {
								fileWriter.write("TTL" + TTL + "\n");
								fileWriter.write(responseIP+ "\n");
								fileWriter.write(differenceTime + " ms"+ "\n");
								ttlNumCount++;
							} else {
								fileWriter.write(differenceTime + " ms"+ "\n");
								ttlNumCount++;
							}

						}
					}
				}
			}
				curPos++;
			}

			fileWriter.close();

		} catch(FileNotFoundException e) {
			System.out.println("Could not open file " + tracerouteFile);
		} catch(IOException e) {
			System.out.println("Error occured while writing to the output file");
		}
	}
}