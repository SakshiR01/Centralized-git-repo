<!DOCTYPE html>
<html lang="en">
<head>
    <title>Contripoint Reminder</title>
</head>
<body style="font-family: 'Poppins', sans-serif;background-image: url('cid:background'); background-size: cover; width: 700px; margin:0 auto;">
    <div style="width: 143px; height: 110px; margin-left: 40%; position: relative;">
            <img style = "margin-top:65px" src="cid:hourglass"/>
    </div>
    <div style=" width: 100%; text-align: center; height: 100%;">
            <div style="font-weight: 600; font-style: normal; font-size: 24px; line-height: 36px; margin-top: 90px; color:black;">
                Hi ${emailTemplate.managerName},
            </div>
            <div style="font-weight: 500; font-style: normal; font-size: 18px; line-height: 27px; color:black;">
                    ${emailTemplate.contributionsPending} contributions pending for your approval.
            </div>

        <#if emailTemplate.contributionCountList[0]??>
            <div style="position: relative; width: 511px; height: 123px; background: #FFFFFF; border-radius: 16px; margin: auto; margin-top: 20px;">
                <div style="width: 25%; height: 100%; float: margin-left; background-position-y: center; background-size:50%; background-position: center; background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:certifications"/>
                </div>
                <div>
                    <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:margin-left ; text-align: margin-left;">
                        Certificates Uploaded <br>
                        <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                            ${emailTemplate.contributionCountList[0].contributionCategorySize} Contributions
                        </span>
                    </span>
                </div>
            </div>
        </#if>

        <#if emailTemplate.contributionCountList[2]??>
            <div style="position: relative; width: 511px; height: 123px; background: #FFFFFF; border-radius: 16px; margin: auto; margin-top: 20px;">
                <div style="width: 25%; height: 100%; float: margin-left; background-position-y: center; background-size:50%; background-position: center; background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:interviews-taken"/>
                </div>
                <div>
                    <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:margin-left ; text-align: margin-left;">
                        Interview Taken <br>
                        <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                            ${emailTemplate.contributionCountList[2].contributionCategorySize} Contributions
                        </span>
                    </span>
                </div>
            </div>
        </#if>

        <#if emailTemplate.contributionCountList[3]??>
            <div style="position: relative;width: 511px;height: 123px;background: #FFFFFF;border-radius: 16px;margin: auto;margin-top: 20px;">
                <div style="width: 25%;height: 100%;float: margin-left;background-position-y: center;background-size:50%;background-position: center;background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:trainings-sessions"/>
                </div>
                <div>
                   <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:margin-left ; text-align: margin-left;">
                       Training Sessions <br>
                       <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                           ${emailTemplate.contributionCountList[3].contributionCategorySize} Contributions
                       </span>
                   </span>
                </div>
            </div>
        </#if>

        <#if emailTemplate.contributionCountList[4]??>
            <div style="position: relative;width: 511px;  height: 123px; background: #FFFFFF; border-radius: 16px; margin: auto; margin-top: 20px;">
                <div style="width: 25%; height: 100%;float: margin-left;background-position-y: center;background-size:50%;background-position: center;background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:mentorship"/>
                </div>
                <div>
                    <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:margin-left ; text-align: margin-left;">
                        Mentorship <br>
                        <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                            ${emailTemplate.contributionCountList[4].contributionCategorySize} Contributions
                        </span>
                    </span>
                </div>
            </div>
        </#if>
        <#if emailTemplate.contributionCountList[5]??>
            <div style="position: relative;   width: 511px; height: 123px;  background: #FFFFFF;  border-radius: 16px; margin: auto;  margin-top: 20px;">
                <div style="width: 25%; height: 100%; float: margin-left; background-position-y: center;  background-size:50%; background-position: center; background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:projects"/>
                </div>
                <div>
                    <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:left ; text-align: left;">
                        Project <br>
                        <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                            ${emailTemplate.contributionCountList[5].contributionCategorySize} Contributions
                        </span>
                    </span>
                </div>
            </div>
        </#if>
        <#if emailTemplate.contributionCountList[6]??>
            <div style="position: relative; width: 511px;  height: 123px;  background: #FFFFFF;  border-radius: 16px;  margin: auto;  margin-top: 20px;">
                <div  style="width: 25%; height: 100%; float: left;  background-position-y: center; background-size:50%; background-position: center; background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:team-building"/>
                </div>
                <div >
                    <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:left ; text-align: left;">
                        Team Building <br>
                        <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                            ${emailTemplate.contributionCountList[6].contributionCategorySize} Contributions
                        </span>
                    </span>
                </div>
            </div>
        </#if>

       <#if emailTemplate.contributionCountList[1]??>
            <div style="position: relative; width: 511px; height: 123px; background: #FFFFFF;  border-radius: 16px; margin: auto; margin-top: 20px;">
                <div style="width: 25%; height: 100%; float: left;  background-position-y: center; background-size:50%; background-position: center; background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:client-feedback"/>
                </div>
                <div >
                    <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:left ; text-align: left;">
                        Client Feedback <br>
                        <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                            ${emailTemplate.contributionCountList[1].contributionCategorySize} Contributions
                        </span>
                    </span>
                </div>
            </div>
       </#if>
       <#if emailTemplate.contributionCountList[7]??>
            <div style="position: relative; width: 511px; height: 123px; background: #FFFFFF; border-radius: 16px; margin: auto; margin-top: 20px;">
                <div style="width: 25%;height: 100%;float: left;  background-position-y: center; background-size:50%; background-position: center; background-repeat: no-repeat;">
                    <img style="margin-top:20px" src="cid:self-development"/>
                </div>
                <div>
                    <span  style="position: absolute; font-weight: 500; font-style: normal; font-size: 16px; line-height: 24px;color: #000000; display: inline-block; margin-top: 32px; float:left ; text-align: left;">
                        Self Development <br>
                        <span  style="position: absolute; font-weight: 400; font-size: 14px; font-style: normal; line-height: 21px; color: rgba(0,0,0,0.6); display: inline-block;">
                            ${emailTemplate.contributionCountList[7].contributionCategorySize} Contributions
                        </span>
                    </span>
                </div>
            </div>
       </#if>

       <a href="http://contripoint.geminisolutions.com.s3-website.ap-south-1.amazonaws.com/#/login" target="blank">
           <button  style="width: 173px; height: 40px; font-size: 14px; font-weight: 600; background: #9B2F9B; border-radius: 6px; color: white; margin-top: 10px; border: none; cursor: pointer;">
                Click to Approve
           </button>
       </a>
       <hr>
       </img>
    </div>
    <span style="font-style: normal; font-weight: normal; margin-left: 26px; margin-top: 10px; font-size: 14px; line-height: 21px; color: #000000">Note :</span>
    <ul type="disk">
        <li style="align-left: 22px;font-weight: 400;font-style: italic;font-size: 14px; line-height: 21px; color: rgba(0,0,0,0.6);">
            Please take necessary action to avoid further repetitive emails
        </li>
        <li style="algn-left: 22px; font-weight: 400;font-style: italic;font-size: 14px;line-height: 21px;color: rgba(0,0,0,0.6);">
            This is an auto generated email from Contripoint, please do not reply
        </li>
    </ul>

</body>
</html>
