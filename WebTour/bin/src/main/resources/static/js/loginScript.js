const signUpButton = document.getElementById("signUp");
const signInButton = document.getElementById("signIn");
const container = document.getElementById("container");

signUpButton.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});

async function handleLogin() {
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
            throw new Error("User not found or request failed");
        }
        const data = await response.json();
		const encrypted = data.password.split("-");
		const isCorrect = await verifyPasswordHMAC(passWord, encrypted[0], encrypted[1]);
		if(isCorrect)
		{
			sessionStorage.setItem("userId", data.userId);
			window.location.href = "/home";
		}
		else alert("Incorrect Password!");
    } catch (error) {
        console.error("Error:", error.message);
    }
}

async function handleRegisterAccont()
{
	const passWord = document.getElementById("password-register").value;
	const encrypted = await encryptPasswordHMAC(passWord);
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

