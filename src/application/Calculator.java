package application;

public class Calculator {
    
    private long minRPM;
    private long maxRPM;
    private long shiftRPM;
    private long rpmStepSize;
    private double userDefinedTireDiameter = 0.0;
    private double rimDiameter; //In inches
    private long tireWidth; //In millimeters
    private long tireAspectRatio; //In percent
    private double finalDriveRatio;
    private long frontSprocketTeeth;
    private long rearSprocketTeeth;
    private int numGears;
    private double[] gearRatios;
    
    //private Parser parser;
    
    private double sprocketGearRatio;
    private double tireDiameter; //In inches
    private double tireRollout; //Tire circumference, in inches
    
    //All in miles per hour
    private double[] minSpeeds;
    private double[] maxSpeeds;
    private double[][] speedsPerGear;
    private double[][] torquePerGear;
    
    
    
    public Calculator(Parser parser) {
        readParsedData(parser);
        
        //Check if an overall tire diameter is already defined
        //If it is defined, set tireDiameter to that number instead of doing this math
        if (userDefinedTireDiameter > 0.1) {
            tireDiameter = userDefinedTireDiameter;
        }
        else {
            tireDiameter = (tireWidth / 25.4) * (tireAspectRatio / 50.0) + rimDiameter;
        }
        
        tireRollout = tireDiameter * Math.PI;
        minSpeeds = new double[numGears];
        maxSpeeds = new double[numGears];
        
        calculateSprocketGearRatio();
        
        //Calculate the minimum speed possible for each gear
        calculateMinSpeeds();
        
        //Calculate the maximum speed possible for each gear
        calculateMaxSpeeds();
        
        //Calculate the speed at every possible rpm for all the given gears
        calculateSpeedsPerGear();
    }
    
    public void calculateSpeedsPerGear() {
        System.out.println("NG " + numGears);
        System.out.println("Max " + maxRPM);
        System.out.println("Min " + minRPM);
        System.out.println("Step " + rpmStepSize);
        System.out.println("FD " + finalDriveRatio);
        System.out.println("Rollout " + tireRollout);
        System.out.println("SG " + sprocketGearRatio);
        System.out.println("G1 " + gearRatios[0]);
        System.out.println("G2 " + gearRatios[1]);
        System.out.println("G3 " + gearRatios[2]);
        System.out.println("G4 " + gearRatios[3]);
        System.out.println("G5 " + gearRatios[4]);
        System.out.println("TD " + tireDiameter);
        System.out.println("FS " + frontSprocketTeeth);
        System.out.println("RS " + rearSprocketTeeth);
        speedsPerGear = new double[numGears][(int) ((maxRPM - minRPM) / rpmStepSize) + 1];
        for (int gear = 0; gear < numGears; gear++) {
            for (long rpm = 0; rpm <= maxRPM - minRPM; rpm += rpmStepSize) {
                speedsPerGear[gear][(int) (rpm / rpmStepSize)] = (((rpm + minRPM)  / gearRatios[gear]) / finalDriveRatio) * tireRollout / sprocketGearRatio / 1056;
            }
        }
    }
    
    /**
     * Copies data read by the parser into the calculator so that it can
     * be read and modified during program execution
     */
    private void readParsedData(Parser parser) {
        minRPM = parser.getMinRPM();
        maxRPM = parser.getMaxRPM();
        shiftRPM = parser.getShiftRPM();
        rpmStepSize = parser.getRPMStepSize();
        userDefinedTireDiameter = parser.getUserDefinedTireDiameter();
        rimDiameter = parser.getRimDiameter();
        tireWidth = parser.getTireWidth();
        tireAspectRatio = parser.getTireAspectRatio();
        finalDriveRatio = parser.getFinalDriveRatio();
        frontSprocketTeeth = parser.getFrontSprocketTeeth();
        rearSprocketTeeth = parser.getRearSprocketTeeth();
        numGears = parser.getNumGears();
        gearRatios = parser.getGearRatios();
    }
    
    /**
     * Updates the tire diameter value. Called after the tire width, aspect
     * ratio, or rim diameter are changed.
     */
    private void updateTireDiameter() {
        if (userDefinedTireDiameter > 0.1) {
            tireDiameter = userDefinedTireDiameter;
        }
        else {
            tireDiameter = (tireWidth / 25.4) * (tireAspectRatio / 50.0) + rimDiameter;
        }
        
        tireRollout = tireDiameter * Math.PI;
        calculateMinSpeeds();
        calculateMaxSpeeds();
    }
    
    private void calculateMinSpeeds() {
        for (int n = 0; n < numGears; n++) {
            minSpeeds[n] = ((minRPM / gearRatios[n]) / finalDriveRatio) * tireRollout / sprocketGearRatio / 1056;
        }
    }
    
    private void calculateMaxSpeeds() {
        for (int n = 0; n < numGears; n++) {
            maxSpeeds[n] = ((maxRPM / gearRatios[n]) / finalDriveRatio) * tireRollout / sprocketGearRatio / 1056;
        }
    }
    
    private void calculateSprocketGearRatio() {
        sprocketGearRatio = ((double) rearSprocketTeeth) / frontSprocketTeeth;
    }
    
    public long calculateRPMAtSpeed(int gear, double speed) {
        return (long) (speed * gearRatios[gear] * finalDriveRatio * sprocketGearRatio * 1056 / tireRollout);
    }
    
    public long getMinRPM() {
        return minRPM;
    }
    
    public void setMinRPM(long minRPM) {
        this.minRPM = minRPM;
        calculateMinSpeeds();
    }
    
    public long getMaxRPM() {
        return maxRPM;
    }
    
    public void setMaxRPM(long maxRPM) {
        this.maxRPM = maxRPM;
        calculateMaxSpeeds();
    }
    
    public long getShiftRPM() {
        return shiftRPM;
    }
    
    public void setShiftRPM(long shiftRPM) {
        this.shiftRPM = shiftRPM;
    }
    
    public long getRPMStepSize() {
        return rpmStepSize;
    }
    
    public double getTireDiameter() {
        return tireDiameter;
    }
    
    public void setUserDefinedTireDiameter(double userDefinedTireDiameter) {
        this.userDefinedTireDiameter = userDefinedTireDiameter;
        updateTireDiameter();
    }
    
    public void setRimDiameter(double rimDiameter) {
        this.rimDiameter = rimDiameter;
        updateTireDiameter();
    }
    
    public void setTireWidth(long tireWidth) {
        this.tireWidth = tireWidth;
        updateTireDiameter();
    }
    
    public void setTireAspectRatio(long tireAspectRatio) {
        this.tireAspectRatio = tireAspectRatio;
        updateTireDiameter();
    }
    
    public double getFinalDriveRatio() {
        return finalDriveRatio;
    }
    
    public void setFinalDriveRatio(double finalDriveRatio) {
        this.finalDriveRatio = finalDriveRatio;
        calculateMinSpeeds();
        calculateMaxSpeeds();
    }
    
    public long getFrontSprocketTeeth() {
        return frontSprocketTeeth;
    }
    
    public void setFrontSprocketTeeth(long frontSprocketTeeth) {
        this.frontSprocketTeeth = frontSprocketTeeth;
        calculateSprocketGearRatio();
        calculateMinSpeeds();
        calculateMaxSpeeds();
    }
    
    public long getRearSprocketTeeth() {
        return rearSprocketTeeth;
    }
    
    public void setRearSprocketTeeth(long rearSprocketTeeth) {
        this.rearSprocketTeeth = rearSprocketTeeth;
        calculateSprocketGearRatio();
        calculateMinSpeeds();
        calculateMaxSpeeds();
    }
    
    public int getNumGears() {
        return numGears;
    }
    
    public double[] getGearRatios() {
        return gearRatios;
    }
    
    public void setGearRatio(int gear, double ratio) {
        this.gearRatios[gear] = ratio;
        calculateMinSpeeds();
        calculateMaxSpeeds();
    }
    
    public double getTireRollout() {
        return tireRollout;
    }
    
    public double[] getMinSpeeds() {
        return minSpeeds;
    }
    
    public double[] getMaxSpeeds() {
        return maxSpeeds;
    }
    
    public double[][] getSpeedsPerGear() {
        return speedsPerGear;
    }

}
