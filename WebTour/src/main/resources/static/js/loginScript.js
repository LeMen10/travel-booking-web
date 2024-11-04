const linkHomePages = {
	"1" :"/admin",
	"2" : "/employee",
	"3" : "/home"
}
const signUpInputs = [
            document.getElementById("full-name-register"),
            document.getElementById("email-register"),
            document.getElementById("user-name-register"),
            document.getElementById("phone-register"),
            document.getElementById("password-register")
        ];
        const signInInputs = [
            document.getElementById("user-name-login"),
            document.getElementById("password-login")
        ];
		const genderRadios = document.getElementsByName("gender");
        const signUpButton = document.getElementById("signUpButton");
        const signInButton = document.getElementById("signInButton");

        function checkInputs() {
            const allFilled = signUpInputs.every(input => input.value.trim() !== "");
			const genderSelected = Array.from(genderRadios).some(radio => radio.checked);
			return (allFilled && genderSelected);
        }

        function checkSignInInputs() {
            const allFilled = signInInputs.every(input => input.value.trim() !== "");
			return allFilled;
        }


document.addEventListener("DOMContentLoaded", function() {
	const signUpNavigateButton = document.getElementById("signUp");
	const signInNavigateButton = document.getElementById("signIn");
	const container = document.getElementById("container");
	
	signUpNavigateButton.addEventListener("click", () => {
	    container.classList.add("right-panel-active");
	});
	
	signInNavigateButton.addEventListener("click", () => {
	    container.classList.remove("right-panel-active");
	});
	
});


async function handleLogin(event) {
	if(!checkSignInInputs()) return;
	event.preventDefault();
    const userName = document.getElementById("user-name-login").value;
    const passWord = document.getElementById("password-login").value;
    
    const url = "http://localhost:8080/get-account/" + userName;

    const request = new Request(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });

    try {
        const response = await fetch(request);
        if (!response.ok) {
           openDialogError("User not found or incorrect password");
		   return;
        }
        const data = await response.json();
		
		const encrypted = data.password.split("-");
		const isCorrect = await verifyPasswordHMAC(passWord, encrypted[0], encrypted[1]);
		if(isCorrect)
		{
			sessionStorage.setItem("userId", data.user.user_id);
			const bookingId =  sessionStorage.getItem("bookingID");
			if(bookingId != null && data.user.role.roleId == 3)
				{
					const isSuccess = await addUserToBooking(data.user.user_id, bookingId);
					if(isSuccess) window.location.href = `/payment/${bookingId}`;
					else openDialogError("Can't found booking ToT");
				} 
			else window.location.href =linkHomePages[data.user.role.roleId + ""];

		}
		else openDialogError("User not found or incorrect password");
    } catch (error) {
        console.error("Error:", error.message);
		openDialogError("500 server error");
    }
}

async function handleRegisterAccont(event)
{
	if(!checkInputs()) return;

	const Password = document.getElementById("password-register").value;
	const fullName = document.getElementById("full-name-register").value;
	const userName = document.getElementById("user-name-register").value;
	const Phone = document.getElementById("phone-register").value;
	const Email = document.getElementById("email-register").value;
	if(!checkFullName(fullName) || !checkPhone(Phone) || !checkUserName(userName) || !checkEmail(Email) || !checkPass(Password)) return;
	event.preventDefault();
	const encrypted = await encryptPasswordHMAC(Password);
	
	const responseCheckUserName = await fetch("http://localhost:8080/get-account/" + userName	, {
		    method: 'GET',
		    headers: {
		        'Content-Type': 'application/json'
		    },
		});
	
	console.log(responseCheckUserName.ok)
	if(responseCheckUserName.ok)
	{
		openDialogError("User Name have existed!");
		return;
	}
	else
	{
	const User = {
		"fullName" : fullName,
		"userName" : userName,
		"Phone" : Phone,
		"Email": Email,
		"Gender" : "Male",
		"Password" : `${encrypted.storedSalt}-${encrypted.storedHash}`
	}
	console.log(JSON.stringify(User));
	const Response = await fetch(`http://localhost:8080/add-account`, {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(User)
	})
	
	if(Response.ok)
	{
		openDialogSuccess("created successfully!");
			
	}
	else openDialogError("500 server error");
	}
}

async function addUserToBooking(userId, bookingId)
{
	const response = await fetch(`http://localhost:8080/api-add-user-to-booking?userId=${userId}&bookingId=${bookingId}`, {
			method: 'PUT',
			headers: {
				"Content-Type": "application/json",
			}
		});

		return response.ok;
}

function checkFullName(fullName)
{
	const regex = /^[a-zA-Z_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêếìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ ]{2,255}$/;
	return regex.test(fullName);
}

function checkUserName(userName)
{
	const regex = /^[a-zA-Z0-9]{6,20}$/;
	return regex.test(userName);
}

function checkPhone(Phone)
{
	const regex = /^0[0-9]{9,11}$|^[+][0-9]{2}[0-9]{8,10}$|^0[0-9]{2}-[0-9]{3,4}-[0-9]{3,4}$|^[+][0-8]{2}-[0-9]{3}-[0-9]{3,4}-[0-9]{3,4}$/;
	return regex.test(Phone);
}

function checkEmail(Email)
{
	const regex = /^[\w.-]+@[a-zA-Z\d.-]+.[a-zA-Z]{2,6}$/;
	return regex.test(Email);
}

function checkPass(Pass)
{
	const regex = /^[a-zA-Z0-9]{6,20}$/;
	return regex.test(Pass);
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


async function verifyPasswordHMAC(passwordInput, storedSalt, storedHash) {
    const passwordBuffer = new TextEncoder().encode(passwordInput);
    const saltBuffer = hexToBuffer(storedSalt);

    const key = await crypto.subtle.importKey(
        "raw",
        saltBuffer,
        { name: "HMAC", hash: { name: "SHA-256" } },
        false,
        ["sign"]
    );
    
    const hash = await crypto.subtle.sign(
        "HMAC",
        key,
        passwordBuffer
    );
    
    const hashHex = bufferToHex(hash);
    
    return (hashHex === storedHash);
}


function bufferToHex(buffer) {
    return Array.from(new Uint8Array(buffer))
        .map(byte => byte.toString(16).padStart(2, '0'))
        .join('');
}

function hexToBuffer(hex) {
    let bytes = [];
    for (let i = 0; i < hex.length; i += 2) {
        bytes.push(parseInt(hex.substr(i, 2), 16));
    }
    return new Uint8Array(bytes);
}

