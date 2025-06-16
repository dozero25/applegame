class HeaderService {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new HeaderService();
        }
        return this.#instance;
    }

    async loadHeader() {
        const header = document.querySelector('.header');
        const principal = await PrincipalApi.getInstance().getPrincipal();

        header.innerHTML += `
        <div class="header-logo">DozeroGameHub</div>
        <div class="auth-buttons">
            ${principal == null
                ? `<a href="/login"><button id="loginBtn">로그인</button></a>`
                : `
                    <span>${principal.nickname}님</span>
                    <a href="/mypage"><button id="mypageBtn">마이페이지</button></a>
                    
                    <a href="/logout"><button id="logoutBtn">로그아웃</button></a>
                `}
        </div>
    `;
    }

}