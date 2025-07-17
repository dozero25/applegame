class HeaderService {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new HeaderService();
        }
        return this.#instance;
    }

    constructor() {
        this.principal = null;
    }

    async loadHeader() {
        const header = document.querySelector('.header');

        PrincipalApi.getInstance().handleOAuthRedirect();

        if (!this.principal) {
            try {
                this.principal = await PrincipalApi.getInstance().getPrincipal();

            } catch (error) {
                console.error("사용자 정보 로드 실패:", error);
                this.principal = null;
            }
        }

        header.innerHTML = `
                <div class="header-logo"><a href="/main">DozeroGameHub</a></div>
                <div class="auth-buttons">
                    ${this.principal == null
                        ? `<a href="/login"><button id="loginBtn">로그인</button></a>`
                        : `
                            <span>${this.principal.nickname}님</span>
                            <a href="/mypage"><button id="mypageBtn">마이페이지</button></a>
                            <button id="logoutBtn">로그아웃</button>
                        `}
                </div>
            `;

        if (this.principal) {
            const logoutBtn = document.getElementById("logoutBtn");
            logoutBtn.addEventListener("click", async (e) => {
                e.preventDefault();
                try {
                    await fetch("/logout", { method: "POST" });

                    localStorage.removeItem("token");

                    window.location.href = "/main";
                } catch (error) {
                    console.error("로그아웃 실패:", error);
                }
            });
        }
    }

}