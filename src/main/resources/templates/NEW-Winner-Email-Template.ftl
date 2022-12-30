<!DOCTYPE html>
<html>

<head>
    <title>Congratulations you have won !</title>
</head>

<body>
    <div
        style="position: relative; margin: 0 auto; width: 600px; height: 130%;  background: #FFF7FF; background-image: url('cid:background'); padding-bottom: 27px;">
        <div><img src="cid:new-winner" alt="email image" style="position: relative; margin-left: 174px; width: 252px; height: 222px; margin-top: 30px;">
        </div>
        <div
            style="position: relative; height: 45px;  font-family: 'Poppins', sans-serif; font-style: normal; font-weight: 600; font-size: 30px; line-height: 45px; text-align: center; color: #264653;">
            Winners Are Out!</div>
        <div
            style="position: relative; margin: 0 auto; width: 500px; height: 50%;  background: #FFFFFF; border-radius: 16px; padding-top: 28px; padding-left: 28px; padding-right: 28px; padding-bottom: 20px; margin-top: 16px;">
            <span
                style="position: relative; height: 22px;  font-family: 'Poppins', sans-serif; font-style: normal; font-weight: 600; font-size: 18px; line-height: 27px; color: #000000;">Hi,
                ${e.employeeName}
            </span>
            <br>
            <br>
            <span
                style="position: relative; width: 493px; height: 49px; font-family: 'Poppins', sans-serif; font-style: normal; font-weight: 500; font-size: 15px; line-height: 169.5%; color: rgba(0, 0, 0, 0.6);">
                We would like to thank you for entering our '<span
                    style="font-family: 'Poppins', sans-serif; font-style: normal; font-weight: 500; font-size: 14px; line-height: 21px; text-align: center; letter-spacing: 0.02em; color: #B361B3;">${e.eventName}</span>'
                contest and making it a huge success. </span>
                <br>
                <br>
            <div
                style="position: relative; width: 491px; height: 100%;  font-family: 'Poppins', sans-serif; font-style: normal; font-weight: normal; font-size: 15px; line-height: 22px; letter-spacing: 0.02em; color: rgba(0, 0, 0, 0.6);">
                Please find the winners listed below :
                <br>
                <br>
                <div
                    style="position: relative; width: 496px; background: #FFFFFF;border: 1px solid rgba(0, 0, 0, 0.1); box-sizing: border-box; border-radius: 16px; padding-left: 30px; padding-top: 24px; padding-bottom: 30px; z-index: 1;">
                    <span
                        style="font-family: 'Poppins', sans-serif; font-style: normal; font-weight: 600; font-size: 18px; line-height: 27px;">WINNERS</span>
                    <br>

                        <table style="border: none; width: 65%; height: 80%; margin-top: 24px; position: relative;">
                            <#list e.listOfRankers as ranker>
                            <tr style="height: 60px;">
                                <td><b>${ranker?counter}<#if ranker?counter ==  1>st<#elseif ranker?counter == 2>nd<#elseif ranker?counter == 3>rd<#else>th</#if></b></td>
                                <td><img src="${ranker.rankerPic}" style="width: 30.96px; height: 31.33px; border: 2px solid #FFFFFF; border-radius: 17px;"></td>
                                <td><b>${ranker.rankerName}</b> <br> ${ranker.rankerDesignation}</td>
                            </tr>
                            </#list>
                        </table>
                        <img src="cid:trophyImg" style="height: 116px; width: 156px; margin-bottom: -10px">
                </div>
                <div style="position: relative; margin-top: 20px;">
                    <span
                        style="font-family: 'Poppins', sans-serif; font-style: normal; font-weight: 500; font-size: 14px; line-height: 21px; text-align: center; letter-spacing: 0.02em; color: #B361B3;">
                        Heartiest Congratulations to all the winners!
                    </span>
                    <br>
                    Please click here to see more details -
                    <a href="http://contripoint.geminisolutions.com.s3-website.ap-south-1.amazonaws.com/#/login"
                        style="text-decoration: none;">
                        Login Contripoint
                    </a>

                    <br>
                    <br> Cheers,
                    <br>
                    <span
                        style="font-family: 'Poppins', sans-serif; font-style: normal; font-weight: 500; font-size: 14px; line-height: 21px; text-align: center; letter-spacing: 0.02em; color: #B361B3;">
                        Contripoint Team!
                    </span>
                </div>
            </div>
        </div>
    </div>
</body>

</html>