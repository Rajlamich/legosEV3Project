# legosEV3 Project

Project Documentation:

Roles:

  Raj Lamichhane
          I created threads to run the line follower and obstacle detection. The program uses the color sensor for line following that controls how the robot moves. The program also utilizes the ultrasonic sensor for collision and to avoid an obstacle while processing in real time.

Bishnu Chaudhary
          I helped debug the code and install the software environment, and then I verified that the line following and obstacle detection were working properly on the robot. I assisted in tuning the various sensor thresholds and motor speeds.

Hom Bahadur Adhikari

          I did assembling the necessary hardware, all sensors and motors were connected, wired, and functioning. The color sensor and ultrasonic sensor worked and helped troubleshoot problems and assist with coding.


Introduction:

The program utilizes the run() function to continuously command the robot. It travels down a line by means of the color sensor, adjusting course where necessary. Whenever an object is detected by the ultrasonic sensor, the robot performs an obstacle avoidance routine that includes stopping, turning, and aligning to resensor the line. The loop of the run() function continually executes these operations until the robot is manually stopped.