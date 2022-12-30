<!DOCTYPE html>
<html lang="en">
<head>
    <title>Reminder Email</title>
</head>
<body>
<div style="position: relative; width: 650px; height: 800px; background: #FFF7FF; margin: auto; background-size:cover; background-image: url('cid:background'); ">


    <div class="flex-column" style="position: center; width: 392px; height: 236.7px; margin: 41px auto;text-align: center;">
        <img src="cid:clock" alt="image" style=" margin-top: 50px;
        margin-right: 64px;">
    </div>


    <div style="position: center; width: 231px; height: 45px; font-family: 'Poppins',sans-serif; font-style: normal; font-weight: 600; font-size: 30px; line-height: 45px; text-align: center; color: #264653; margin: 0 auto;">
        HURRY UP!

    </div>
    <div style="text-align: center;">
    <span style="position: relative; font-family: 'Poppins',sans-serif; font-style: normal; font-weight: 500; font-size: 22px; line-height: 40px;   letter-spacing: 0.02em; color: #B361B3; margin: 0 auto">
            ${EmailReminderTemplate.eventName} event is ending today</span>
    </div>
    <div style="position: relative; width: 551px; height: 349px; background: #FFFFFF; border-radius: 16px; margin: 32px auto; padding: 8px;">
        <div style="position: relative; width: 400px; height: 22px; font-family: 'Poppins',sans-serif; font-style: normal; font-weight: 600; font-size: 18px; line-height: 27px; color: #000000; margin: 28px 404px 30px 28px;">
            Hi ${EmailReminderTemplate.employeeName},
        </div>
        <div style="position: relative; width: 491px; height: 162px; font-family: 'Poppins',sans-serif; font-style: normal; font-weight: normal; font-size: 15px; line-height: 22px; letter-spacing: 0.02em; color: rgba(0, 0, 0, 0.6); margin: 0 auto;">
            We are about to close our event   <span style=" color: #B361B3;">'${EmailReminderTemplate.eventName}'</span> .
            So do lock your participations if not already.
            <br>
            <br>
            This email is a reminder for you to work on incrementing your
            contribution points in order to win <span style="color:#D78811">exciting prizes.</span>
            <br>
            <br>
            Kindly login to the website for more details:<a
                    href="http://contripoint.geminisolutions.com.s3-website.ap-south-1.amazonaws.com/#/login">Login Contripoint
            </a>. <br>
            <br>
            Wishing you all the best,
            <br>
            <span
                    style="font-family: ''Poppins',sans-serif', sans-serif; font-style: normal; font-weight: 500; font-size: 14px; line-height: 21px; text-align: center; letter-spacing: 0.02em; color: #B361B3;">Contripoint Team!</span>
        </div>
    </div>
</div>
</body>
</html>