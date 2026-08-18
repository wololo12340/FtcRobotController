package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="MockOpMode", group="Iterative OpMode")
public class MockOpMode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        //IMU initialization
        IMU imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "FL");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "BL");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "FR");
        rightBackDrive  = hardwareMap.get(DcMotor.class, "BR");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop(double botAngle) {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftFrontPower;
        double rightFrontPower;
        double leftBackPower;
        double rightBackPower;

        //Polar Drive
        double radius = Math.sqrt(gamepad1.left_stick_x*gamepad1.left_stick_x+gamepad1.left_stick_y*gamepad1.left_stick_y);
        double dzval = 0.5;
        double exp = 4.0;
        double speed = Math.pow(((radius-dzval)/(1-dzval)), exp)/1.1;
        if(radius < dzval){
            speed = 0;
        }
        double angle = Math.atan2(gamepad1.left_stick_y,-gamepad1.left_stick_x);
        double strafe = speed * Math.cos(angle);
        double forward = speed * Math.sin(angle);

        double rotation = -gamepad1.right_stick_x;
        // Calculate wheel powers
        double y = Math.cos(botAngle) * forward + Math.sin(botAngle) * strafe;
        double x = Math.cos(botAngle) * strafe - Math.sin(botAngle) * forward;
        leftFrontPower = forward + strafe + rotation;
        rightFrontPower = forward - strafe - rotation;
        leftBackPower = forward - strafe + rotation;
        rightBackPower = forward + strafe - rotation;

        // Send calculated power to wheels
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        // Slow strafe
        double strafeSpeed = speed/2;
        if (gamepad1.right_trigger_pressed){
            strafe = strafeSpeed * Math.cos(angle);
            forward = strafeSpeed * Math.sin(angle);
        }
        // Drift Mode
        


        // Make a way to shoot balls using IntakeMotor1, IntakeMotor2, and ShootMotor


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left front (%.2f), right front(%.2f), left back(%.2f), right back(%.2f)", leftFrontPower, rightFrontPower, leftBackPower, rightBackPower);
        telemetry.addData("X & Y", "Left X (%.2f), Left Y(%.2f), Right X(%.2f), Right Y(%.2f)", gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_stick_y);
        telemetry.addData("", "Forward (%.2f), Strafe (%.2f), Rotation (%.2f), radius (%.2f), angle (%.2f), speed (%.2f)", forward, strafe, rotation, radius, angle, speed);
    }

    public

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
