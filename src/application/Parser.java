package application;

import org.json.simple.JSONObject;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.*;

public class Parser {
    
    private long minRPM;
    private long maxRPM;
    private long shiftRPM;
    private long rpmStepSize;
    private double userDefinedTireDiameter;
    private double rimDiameter; //In inches
    private long tireWidth; //In millimeters
    private long tireAspectRatio; //In percent
    private double finalDriveRatio;
    private long frontSprocketTeeth;
    private long rearSprocketTeeth;
    private int numGears;
    
    private double[] gearRatios = new double[6];
    
    public Parser() {
        this("configs/defaults.json");
    }
    
    /**
     * Parses all the data within configs/config.json
     */
    public Parser(String filename) {
        
        JSONObject json = null;
        
        try {
            FileReader reader = new FileReader(filename);
            json = (JSONObject) new JSONParser().parse(reader);
            reader.close();
        }
        catch (IOException e) {
            System.err.println(
                "Error: Could not find config.json in GearRatioCalc/configs\n\n"
                + "Solutions:\n"
                + "Check that the configs folder is in the same folder as gearratiocalc.jar\n"
                + "Make sure configs.json exists");
        }
        catch (ParseException e) {
            System.err.println(
                "Error: JSON data in config.json is incorrectly formatted.\n\n"
                + "Solutions:\n"
                + "Check that there are no missing values in config.json\n"
                + "Make sure the data follows the same format as backup.json");
        }
        
        if (json != null) {
            minRPM = (long) json.get("minRPM");
            maxRPM = (long) json.get("maxRPM");
            shiftRPM = (long) json.get("shiftRPM");
            rpmStepSize = (long) json.get("rpmStepSize");
            
            userDefinedTireDiameter = (double) json.get("userDefinedTireDiameter");
            rimDiameter = (double) json.get("rimDiameter");
            tireWidth = (long) json.get("tireWidth");
            tireAspectRatio = (long) json.get("tireAspectRatio");
            
            finalDriveRatio = (double) json.get("finalDriveRatio");
            frontSprocketTeeth = (long) json.get("frontSprocketTeeth");
            rearSprocketTeeth = (long) json.get("rearSprocketTeeth");
            
            gearRatios[0] = (double) json.get("gear1");
            gearRatios[1] = (double) json.get("gear2");
            gearRatios[2] = (double) json.get("gear3");
            gearRatios[3] = (double) json.get("gear4");
            gearRatios[4] = (double) json.get("gear5");
            gearRatios[5] = (double) json.get("gear6");
            
            for (int n = 0; n < gearRatios.length; n++) {
                if (gearRatios[n] > 0.01) {
                    numGears++;
                }
            }
        }
    }
    
    /**
     * Gets the engine's minimum RPM
     * 
     * @return The engine's minimum RPM
     */
    public long getMinRPM() {
        return minRPM;
    }
    
    /**
     * Gets the engine's maximum RPM
     * 
     * @return The engine's maximum RPM
     */
    public long getMaxRPM() {
        return maxRPM;
    }
    
    /**
     * Gets the RPM at which the driver should aim to shift up at
     * 
     * @return The RPM at which the driver should shift up at
     */
    public long getShiftRPM() {
        return shiftRPM;
    }
    
    public double getUserDefinedTireDiameter() {
        return userDefinedTireDiameter;
    }
    
    public double getRimDiameter() {
        return rimDiameter;
    }
    
    public long getTireWidth() {
        return tireWidth;
    }
    
    public long getTireAspectRatio() {
        return tireAspectRatio;
    }
    
    public double getFinalDriveRatio() {
        return finalDriveRatio;
    }
    
    public long getFrontSprocketTeeth() {
        return frontSprocketTeeth;
    }
    
    public long getRearSprocketTeeth() {
        return rearSprocketTeeth;
    }
    
    public double[] getGearRatios() {
        return gearRatios;
    }
    
    public int getNumGears() {
        return numGears;
    }
    
    public long getRPMStepSize() {
        return rpmStepSize;
    }
    

}
