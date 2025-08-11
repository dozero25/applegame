class PrincipalApi {
    static #instance = null;
    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new PrincipalApi();
        }
        return this.#instance;
    }

    async getPrincipal() {
        const token = localStorage.getItem("token");

        if (!token) {
            console.warn("토큰이 없습니다. 로그인하세요.");
            return null;
        }

        try {
            const response = await fetch("/api/user/principal", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("token")}`,
                    "Content-Type": "application/json"
                },
                credentials: "include",
            });

            if (!response.ok) {
                console.error("사용자 정보 요청 실패:", response.status);
                return null;
            }

            const data = await response.json();
            return data.data || data;
        } catch (error) {
            console.error("사용자 정보 요청 중 오류:", error);
            return null;
        }
    }

    handleOAuthRedirect() {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get("token");
        
        if (token) {
            localStorage.setItem("token", token);
            history.replaceState({}, document.title, window.location.pathname);
        }
    }
}