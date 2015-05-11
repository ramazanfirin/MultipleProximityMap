package in.wptrafficanalyzer.multipleproximitymapv2;

import android.bluetooth.BluetoothDevice;

public class BLEConnectionPreference {

	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public int getMajor() {
		return major;
	}


	public void setMajor(int major) {
		this.major = major;
	}


	public int getMinor() {
		return minor;
	}


	public void setMinor(int minor) {
		this.minor = minor;
	}


	public int getTxpower() {
		return txpower;
	}


	public void setTxpower(int txpower) {
		this.txpower = txpower;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}
	BluetoothDevice bluetoothDevice;
	int rssi; 
	byte[] scanRecord;
	String uuid;
	int major;
	int minor;
	int txpower;
	double distance;
	double estimateDistance;
	
	
	
	
	public double getEstimateDistance() {
		return estimateDistance;
	}


	public void setEstimateDistance(double estimateDistance) {
		this.estimateDistance = estimateDistance;
	}


	public BLEConnectionPreference(BluetoothDevice bluetoothDevice, int rssi,byte[] scanRecord) {
		super();
		this.bluetoothDevice = bluetoothDevice;
		this.rssi = rssi;
		this.scanRecord = scanRecord;
		parseData(scanRecord,rssi);
	}
	
	
	public void parseData(byte[] scanRecord,double rssi){
    	int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                patternFound = true;
                break;
            }
            startByte++;
        }
        
        if (patternFound) {
            //Convert to hex String
            byte[] uuidBytes = new byte[16];
            System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
            String hexString = BLEUtils.bytesToHex(uuidBytes);
            
            //Here is your UUID
            uuid =  hexString.substring(0,8) + "-" + 
                    hexString.substring(8,12) + "-" + 
                    hexString.substring(12,16) + "-" + 
                    hexString.substring(16,20) + "-" + 
                    hexString.substring(20,32);
            
            //Here is your Major value
            major = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);
            
            //Here is your Minor value
            minor = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);
            
            
            txpower = (scanRecord[startByte+24]);
            distance  = BLEUtils.calculateAccuracy(txpower,rssi);
            
            System.out.println("bitti");
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public BluetoothDevice getBluetoothDevice() {
		return bluetoothDevice;
	}
	public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
		this.bluetoothDevice = bluetoothDevice;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	public byte[] getScanRecord() {
		return scanRecord;
	}
	public void setScanRecord(byte[] scanRecord) {
		this.scanRecord = scanRecord;
	}
	
	
}
