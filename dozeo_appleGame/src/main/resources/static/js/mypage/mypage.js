window.onload = () => {
    HeaderService.getInstance().loadHeader();
    MypageService.getInstacne().loadMyPageInfo();
}

class MypageApi {
    static #instance = null;

    static getInstacne() {
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
                const data = await PrincipalApi.getInstance().getPrincipal();
                HeaderService.getInstance().principal = null;
                await HeaderService.getInstance().loadHeader();
            }

            return data;

        } catch (error) {
            console.error("updateMyPageInfo error:", error.message);
            return null;
        }
    }

}

class MypageService {
    static #instance = null;

    static getInstacne() {
        if (this.#instance == null) {
            this.#instance = new MypageService();
        }
        return this.#instance;
    }

    async loadMyPageInfo() {
        const myPage = document.querySelector(".mypage-container");
        const data = await PrincipalApi.getInstance().getPrincipal();

        console.log(data.type);

        myPage.innerHTML = `
            <div id="userUpdateForm">
                <div class="form-group">
                    <label for="username">아이디</label>
                    <input type="text" id="username" name="username" autocomplete="off" placeholder="아이디 입력" value="${data.username}" disabled>
                </div>
                <div class="form-group">
                    <label for="nickname">닉네임</label>
                    <input type="text" id="nickname" name="nickname" autocomplete="off" placeholder="닉네임 입력" value="${data.nickname}" disabled>
                </div>

                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" id="email" name="email" autocomplete="off" placeholder="이메일 입력" value="${data.email}" disabled>
                </div>

                <div class="btn-container">
                    <button type="button" class="btn btn-update">정보 수정</button>
                    <button type="button" class="btn btn-update-password">비밀번호 변경</button>
                    <button type="button" class="btn btn-social-unlink">소셜 연동 해제</button>
                </div>
            </div>
        `;

        this.updateMyPageInfo();
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
                await MypageApi.getInstacne().submitMyInfo(this.collectFromData());
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


}

