package application;

public class TopSpeedSolver {
    
    private Calculator calc;
    private double desiredTopSpeed = 1;
    private int frontSprocketTeeth = 1;
    private int rearSprocketTeeth = 1;
    private double finalDriveRatio = 1;
    private double sprocketGearRatio;
    private boolean useOvershootMode = true;
    
    //Top speed undershoot > round up rear sprocket teeth or round down front sprocket teeth
    //Top speed overshoot > round down rear sprocket teeth or round up front sprocket teeth
    
    public TopSpeedSolver(Calculator calc) {
        this.calc = calc;
    }
    
    public double calculateTopSpeed() {
        desiredTopSpeed = ((calc.getMaxRPM() / calc.getGearRatios()[calc.getNumGears() - 1]) / finalDriveRatio) * calc.getTireRollout() / ((double) rearSprocketTeeth / frontSprocketTeeth) / 1056;
        return desiredTopSpeed;
    }
    
    public double calculateFinalDriveRatio() {
        finalDriveRatio = (double) (calc.getMaxRPM() * calc.getTireRollout()) / (calc.getGearRatios()[calc.getNumGears() - 1] * desiredTopSpeed * sprocketGearRatio * 1056);
        return finalDriveRatio;
    }
    
    public int calculateFrontSprocketTeeth() {
        double calculatedValue = (rearSprocketTeeth * calc.getGearRatios()[calc.getNumGears() - 1] * finalDriveRatio * desiredTopSpeed * 1056) / ((double) (calc.getMaxRPM() * calc.getTireRollout()));
        
        if (useOvershootMode) {
            frontSprocketTeeth = (int) Math.ceil(calculatedValue);
        }
        else {
            frontSprocketTeeth = (int) Math.floor(calculatedValue);
        }
        
        return frontSprocketTeeth;
    }
    
    public int calculateRearSprocketTeeth() {
        double calculatedValue = (frontSprocketTeeth * calc.getMaxRPM() * calc.getTireRollout()) / ((double) (calc.getGearRatios()[calc.getNumGears() - 1] * finalDriveRatio * desiredTopSpeed * 1056));
        
        if (useOvershootMode) {
            rearSprocketTeeth = (int) Math.floor(calculatedValue);
        }
        else {
            rearSprocketTeeth = (int) Math.ceil(calculatedValue);
        }
        
        return rearSprocketTeeth;
    }
    
    public void setDesiredTopSpeed(double topSpeed) {
        desiredTopSpeed = topSpeed;
    }
    
    public double getDesiredTopSpeed() {
        return desiredTopSpeed;
    }
    
    public void setFrontSprocketTeeth(int frontSprocketTeeth) {
        this.frontSprocketTeeth = frontSprocketTeeth;
        sprocketGearRatio = calculateSprocketGearRatio();
    }
    
    public int getFrontSprocketTeeth() {
        return frontSprocketTeeth;
    }
    
    public void setRearSprocketTeeth(int rearSprocketTeeth) {
        this.rearSprocketTeeth = rearSprocketTeeth;
        sprocketGearRatio = calculateSprocketGearRatio();
    }
    
    public int getRearSprocketTeeth() {
        return rearSprocketTeeth;
    }
    
    public void setFinalDriveRatio(double finalDriveRatio) {
        this.finalDriveRatio = finalDriveRatio;
    }
    
    public double getFinalDriveRatio() {
        return finalDriveRatio;
    }
    
    public void setCalculationMode(boolean useOvershootMode) {
        this.useOvershootMode = useOvershootMode;
    }
    
    private double calculateSprocketGearRatio() {
        return ((double) rearSprocketTeeth) / frontSprocketTeeth;
    }

}
