function navigateLoginForm ()
{
	window.location.href = "/";
}
async function handleSendMailPassword(event) {
	event.preventDefault();
	const userName = document.getElementById("user-name-forgot").value;
	
	const responseCheckUserName = await fetch("http://localhost:8080/get-account/" + userName, {
			    method: 'GET',
			    headers: {
			        'Content-Type': 'application/json'
			    },
			});
		
	if(!responseCheckUserName.ok)
	{
		alert("User Name Don't existed!");
		return;
	}
	const data = await responseCheckUserName.json();
	console.log(data)
	const emailData = {
	    to: data.user.email,
	    subject: "Test Email",
	    body: "This is a test email sent from JavaScript!"
	};

	const responseSendMail = await fetch('http://localhost:8080/send-mail', {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(emailData)
	})
	console.log(responseSendMail)
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
