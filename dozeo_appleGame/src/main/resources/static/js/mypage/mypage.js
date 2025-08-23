window.onload = () => {
    HeaderService.getInstance().loadHeader();
    MypageService.getInstance().loadMyPageInfo();
    MypageService.getInstance().showInfoScoreList();

}

class MypageApi {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new MypageApi();
        }
        return this.#instance;
    }

    async submitMyInfo(updateData) {
        try {
            const token = localStorage.getItem("token");

            if (!token) {
                console.error("토큰이 없습니다. 로그인 먼저 필요.");
                return;
            }
            const response = await fetch("api/user/info/update", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                credentials: "include",
                body: JSON.stringify(updateData)
            });

            const data = await response.json().catch(() => null);

            if (!response.ok) {
                const errorMessage = (data && data.message) || "Update failed";
                throw new Error(errorMessage);
            } else {
                await PrincipalApi.getInstance().getPrincipal();
                HeaderService.getInstance().principal = null;
                await HeaderService.getInstance().loadHeader();
            }

            return data;

        } catch (error) {
            console.error("updateMyPageInfo error:", error.message);
            return null;
        }
    }

    async unlinkOauth2(provider, id) {
        try {
            const token = localStorage.getItem("token");

            if (!token) {
                console.error("토큰이 없습니다. 로그인 먼저 필요.");
                return;
            }

            const response = await fetch(`/api/user/oauth2/unlink`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                credentials: "include",
                body: JSON.stringify({ provider, accessToken, id }),
            });

            const data = response.json();
            return data;
        } catch (error) {
            console.error("소셜 로그인 연동 해제 중 예외 발생 : ", error)
        }
    }

    async changePassword(updateData) {
        try {
            const token = localStorage.getItem("token");

            if (!token) {
                console.error("토큰이 없습니다. 로그인 먼저 필요.");
                return;
            }
            const response = await fetch("api/user/change/password", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                credentials: "include",
                body: JSON.stringify(updateData)
            });

            const data = await response.json().catch(() => null);
            if (!response.ok) {
                const errorMessage = (data && data.message) || "Failed";
                throw new Error(errorMessage);
            }

            return data;

        } catch (error) {
            console.error("비밀번호 변경 실패 :", error.message);
            return null;
        }
    }

    async getUserScoreList() {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                console.error("토큰이 없습니다. 로그인 먼저 필요.");
                return;
            }

            const response = await fetch("/api/score/list", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                credentials: "include",
            });

            if (!response.ok) {
                console.error("사용자 정보 요청 실패:", response.status);
                return null;
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error("사용자 정보 요청 중 오류:", error);
            return null;
        }
    }

}

class MypageService {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new MypageService();
        }
        return this.#instance;
    }

    async loadMyPageInfo() {
        const myPage = document.querySelector(".mypage-container");
        const data = await PrincipalApi.getInstance().getPrincipal();

        const userType = this.getUserType(data.type);
        const html = this.renderUserInfoForm(data, userType);

        myPage.innerHTML = html;
        this.updateMyPageInfo();
        ComponentEvent.getInstance().clickEventOfUnlinkOauth2();
        ComponentEvent.getInstance().clickEventUpdatePasswordBtn();
    }

    getUserType(type) {
        return {
            isGuest: type === "guest",
            isOAuth: type === "oauth2",
            isGeneral: type === "local",
        };
    }

    renderUserInfoForm(data, { isGuest, isOAuth, isGeneral }) {
        const isInfoEditable = isGeneral;
        const showPasswordButton = isGeneral;
        const showSocialUnlinkButton = isOAuth;

        return `
            <div id="userUpdateForm">
            ${isOAuth !== true ? `
                <div class="form-group">
                    <label for="username">아이디</label>
                    <input type="text" id="username" name="username" autocomplete="off" value="${data.username}" disabled>
                </div>
                ` : ''}
                <div class="form-group">
                    <label for="nickname">닉네임</label>
                    <input type="text" id="nickname" name="nickname" autocomplete="off" disabled value="${data.nickname}" ${isInfoEditable ? "" : "disabled"}>
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" id="email" name="email" autocomplete="off" disabled value="${data.email}" ${isInfoEditable ? "" : "disabled"}>
                </div>

                <div class="btn-container">
                    ${isInfoEditable ? this.renderUpdateButton() : ""}
                    ${showPasswordButton ? this.renderPasswordButton() : ""}
                    ${showSocialUnlinkButton ? this.renderSocialUnlinkButton() : ""}
                </div>
            </div>
        `;
    }

    renderUpdateButton() {
        return `<button type="button" class="btn btn-update">정보 수정</button>`;
    }

    renderPasswordButton() {
        return `<button type="button" class="btn btn-update-password">비밀번호 변경</button>`;
    }

    renderSocialUnlinkButton() {
        return `<button type="button" class="btn btn-social-unlink">소셜 연동 해제</button>`;
    }


    async updateMyPageInfo() {
        const formGroups = document.querySelectorAll(".form-group");
        const updateBtn = document.querySelector(".btn-update");

        let isEditMode = false;

        updateBtn.addEventListener("click", async (e) => {
            e.preventDefault();

            if (!isEditMode) {
                formGroups.forEach(group => {
                    const input = group.querySelector("input");
                    if (input) {
                        input.disabled = false;
                    }
                });

                updateBtn.textContent = "정보 수정 완료";
                isEditMode = true;
            } else {

                formGroups.forEach(group => {
                    const input = group.querySelector("input");
                    if (input) {
                        input.disabled = true;
                    }
                });
                await MypageApi.getInstance().submitMyInfo(this.collectFromData());
                updateBtn.textContent = "정보 수정";
                isEditMode = false;
            }
        });
    }

    collectFromData() {
        return {
            username: document.getElementById("username").value,
            nickname: document.getElementById("nickname").value,
            email: document.getElementById("email").value
        }
    }

    async showInfoScoreList() {
        const rankContainer = document.querySelector(".mypage-article");
        const response = await MypageApi.getInstance().getUserScoreList();

        rankContainer.innerHTML = ``;

        let html = "";
        response.data.forEach(r => {
            html += `
            <article class="ranking-grid">
                <div class="ranking-card">
                    <label class="game-type">${r.gameType}</label>
                    <p class="game-point">내 점수 : ${r.points}점</p>
                    <p class="game-rank">내 순위 : ${r.ranking}위</p>
                </div>
            </article>
        `;
        });

        rankContainer.innerHTML = html;
    }


}

class ComponentEvent {
    static #instacne = null;

    static getInstance() {
        if (this.#instacne == null) {
            this.#instacne = new ComponentEvent();
        }
        return this.#instacne;
    }

    async clickEventOfUnlinkOauth2() {
        const unlinkBtn = document.querySelector(".btn-social-unlink");

        if (!unlinkBtn) {
            console.warn("소셜 연동 해제 버튼이 없습니다.");
            return;
        }

        unlinkBtn.addEventListener("click", async () => {
            try {
                const data = await PrincipalApi.getInstance().getPrincipal();
                await MypageApi.getInstance().unlinkOauth2(data.provider, data.id);
                window.location.href = "/";
                localStorage.removeItem("token");
            } catch (error) {
                console.error(error);
            }

        });
    }

    async clickEventUpdatePasswordBtn() {
        const updateBtn = document.querySelector(".btn-update-password");

        const modal = document.getElementById('modal');
        const closeModalBtn = document.getElementById('closeModalBtn');
        const confirmBtn = document.getElementById('confirmBtn');
        const currentPasswordInput = document.getElementById('currentPassword');
        const changePasswordInput = document.getElementById('changePassword');

        updateBtn.addEventListener('click', () => {
            modal.style.display = 'flex';
            currentPasswordInput.value = '';
            changePasswordInput.value = '';
            currentPasswordInput.focus();
        });

        closeModalBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });

        window.addEventListener('click', (event) => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });

        confirmBtn.addEventListener('click', async () => {
            const data = await this.inputData();
            MypageApi.getInstance().changePassword(data);

            this.showToast("비밀변호 변경이 완료되었습니다.");
            setTimeout(() => {
                modal.style.display = 'none';
            }, 1500);

        });
    }

    async inputData() {
        return {
            currentPassword: document.getElementById('currentPassword').value,
            changePassword: document.getElementById('changePassword').value
        }
    }

    showToast(message) {
        const container = document.getElementById("toast-container");
        const toast = document.createElement("div");
        toast.className = "toast";
        toast.textContent = message;
        container.appendChild(toast);

        setTimeout(() => {
            toast.remove();
        }, 1500);
    }
}
