<%@ taglib uri="jstl-core" prefix="c" %>

 <%/* Note on NS_ERROR_NOT_AVAILABLE. The code discards NS_ERROR_NOT_AVAILABLE messages which
 addresses issues with FF when user sends a AJAX request when previous is not completed yet
 such as user refreshes the page */%>

        function setupDWR(){
            DWRUtil.useLoadingMessage();

            var prompt =    '\nThe error is logged and our support team will be informed.';
            prompt+=    '\nWe encourage you to submit a Defect report.';
            prompt+=    '\nYour report will ensure that the problem will be resolved promptly.';
            prompt+=    '\nPlease help us to improve the application!';
            var handler = function(message, exception) {
                //make sure that flag submitted is reset.
                resetAlreadySubmitted();
                var str;
                if(exception && exception.businessMessage){          //IWM BusinessException was thrown
                    alert(exception.businessMessage);
                    return;
                }

                if (typeof message == "object") {
                    if(message.description)
                        str = "Error: " + message.description;
                    else if(message.message)
                        str = "Error: " + message.message;
                    else if(message.name)
                        str = "Error: " + message.name;
                    else if(message)
                        str = "Error: " + message;
                }
                else {
                    str = message;
                }

                if(str.match(/NS_ERROR_NOT_AVAILABLE/)) return;

                if(confirm(str+prompt)){
                    openWindow("<c:url value='/DefectReport.do?forward=read'/>",'errorreport', 570,960);
                }

            }
            DWREngine.setErrorHandler(handler);
        }

        callOnLoad(setupDWR);



