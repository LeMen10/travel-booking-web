function navigateLoginForm ()
{
	window.location.href = "/";
}
async function handleSendMailPassword(event) {
	const userName = document.getElementById("user-name-forgot").value;
	if(userName.trim() == "") return;
	event.preventDefault();
	showLoading();
	const responseCheckUserName = await fetch("http://localhost:8080/get-account/" + userName, {
			    method: 'GET',
			    headers: {
			        'Content-Type': 'application/json'
			    },
			});
		
	if(!responseCheckUserName.ok)
	{
		hideLoading();
		openDialogError("User Name Don't existed!");
		return;
	}
	const data = await responseCheckUserName.json();
	
	const newPassword = generateRandomString(6);
	
	const encrypted = await encryptPasswordHMAC(newPassword);
	
	const isUpdatedPass = await updatePassword(data.accountId, `${encrypted.storedSalt}-${encrypted.storedHash}`);
	
	if(!isUpdatedPass)
	{
		hideLoading();
		openDialogError("New password can't created!");
		return;
	}
		
	const emailData = {
	    to: data.user.email,
	    subject: "Test Email",
	    body: `Dear ${data.user.fullName}, \nNew password for account ${data.userName} is <strong>${newPassword}</strong>.`
	};

	const responseSendMail = await fetch('http://localhost:8080/send-mail', {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(emailData)
	})
	console.log(responseSendMail);
	if(responseSendMail.ok)
	{
		hideLoading();
		openDialogSuccess("New password has been sent to account's email.");
	}
	else
	{
		hideLoading();
		openDialogError("Email don't exist!");
	}
}

async function updatePassword(accountId, newPassword)
{
	const responseUpdatePassword = await fetch(`http://localhost:8080/api-update-new-password?accountId=${accountId}&newPassword=${newPassword}`, {
		    method: 'PUT',
		    headers: {
		        'Content-Type': 'application/json'
		    },
		})
		if(responseUpdatePassword.ok)
		{
			return true;
		}
		else
		{
			return false;
		}
}
function generateRandomString(length) {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

async function encryptPasswordHMAC(passwordInput) {
    const salt = crypto.getRandomValues(new Uint8Array(16)); 
    const passwordBuffer = new TextEncoder().encode(passwordInput);
    
    // Sử dụng salt và HMAC với SHA-256
    const key = await crypto.subtle.importKey(
        "raw",
        salt,
        { name: "HMAC", hash: { name: "SHA-256" } },
        false,
        ["sign"]
    );
    
    const hash = await crypto.subtle.sign(
        "HMAC",
        key,
        passwordBuffer
    );
    
    return { "storedHash": bufferToHex(hash), "storedSalt": bufferToHex(salt) };
}

function bufferToHex(buffer) {
    return Array.from(new Uint8Array(buffer))
        .map(byte => byte.toString(16).padStart(2, '0'))
        .join('');
}
