const userData = {
    username: "",
    password: "",
    email: "",
    nickname: ""
}

class RegisterApi {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new RegisterApi();
        }
        return this.#instance;
    }

    async registerUserInfo(userData) {
        try {
            const response = await fetch(`http://localhost:8000/api/user/signup`, {
                method: "POST",
                headers: { "Content-type": "application/json" },
                body: JSON.stringify(userData)
            });

            const data = await response.json();

            if (!response.ok) {
                const errorData = await response.json();
                if (errorData.registerError) {
                    throw new Error(errorData.registerError);
                }
            }

            return data;

        } catch (error) {
            console.log("registerUserInfo", error)
            return error;
        }
    }

    async guestLoginForRegister() {
        try {
            const response = await fetch(`http://localhost:8000/api/user/guest/signup`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            const data = await response.json();

            return data;
        } catch (error) {
            console.error("Guest Login Error", error);
            return error;
        }
    }
}



class RegisterService {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new RegisterService();
        }
        return this.#instance;
    }

    saveUserInfo() {
        const form = document.querySelector('.signup-form');
        const errorBox = document.querySelector(".error-message");

        form.addEventListener("submit", async (e) => {
            e.preventDefault();

            userData.username = document.getElementById("reg_username").value.trim();
            userData.password = document.getElementById("reg_password").value.trim();
            userData.email = document.getElementById("reg_email").value.trim();
            userData.nickname = document.getElementById("reg_nickname").value.trim();

            try {
                const result = await RegisterApi.getInstance().registerUserInfo(userData);

                if (result.data === true) {
                    this.showToast(userData.nickname + "님 회원가입이 완료되었습니다! 🎉");
                    setTimeout(() => {
                        window.location.href = "/login";
                    }, 1500);
                } else {
                    const messages = result?.registerError || "회원가입에 실패했습니다.";
                    errorBox.textContent = messages;
                }
            } catch (error) {
                errorBox.textContent = "서버오류가 발생했습니다.";
                console.error(error);
            }
        })
    }

    showToast(message) {
        const container = document.getElementById("toast-container");
        const toast = document.createElement("div");
        toast.className = "toast";
        toast.textContent = message;
        container.appendChild(toast);

        setTimeout(() => {
            toast.remove();
        }, 3000);
    }

    ClickEventForGuestLogin(){
        const guestLoginBtn = document.querySelector(".guest-input-button");

        guestLoginBtn.addEventListener("click", async () => {

            try {
                const result = await RegisterApi.getInstance().guestLoginForRegister();      

                if(result.data && result.data.token){
                    localStorage.setItem("token", result.data.token);
                    localStorage.setItem("username", result.data.username);

                    window.location.href = "/main";
                } else {
                    console.error("게스트 로그인 실패", data);
                }
            } catch (error) {
                console.error("게스트 로그인 중 에러발생", error);
            }  
        });
    }
}