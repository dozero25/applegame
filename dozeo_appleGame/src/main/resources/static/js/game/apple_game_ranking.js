window.onload = () => {
    AppleGameRankingService.getInstance().showInfoAppleGameRanking();
}

class AppleGameRankingMain {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new AppleGameRankingMain();
        }
        return this.#instance;
    }

    async getAllUserRankingOfGameType(gameType) {
        try {
            const response = await fetch(`/api/score/ranking/${gameType}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
            });

            if (!response.ok) {
                console.error("사용자 정보 요청 실패:", response.status);
                return null;
            }

            const data = response.json();
            return data;
        } catch (error) {
            console.error("사용자 정보 요청 중 오류:", error);
            return null;
        }
    }
}


class AppleGameRankingService {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new AppleGameRankingService();
        }
        return this.#instance;
    }

    async showInfoAppleGameRanking() {
        const normal_data = await AppleGameRankingMain.getInstance().getAllUserRankingOfGameType("사과게임");
        const challenge_data = await AppleGameRankingMain.getInstance().getAllUserRankingOfGameType("사과게임_챌린지모드");

        const rankingListNormal = document.querySelector(".ranking-list.normal");
        const rankingListChallenge = document.querySelector(".ranking-list.challenge");

        let html_normal = "";
        let html_challenge = "";

        for (let i = 1; i <= 10; i++) {
            const user = normal_data.data[i - 1];
            const nickname = user ? user.nickname : "";
            const points = user ? user.points : "";

            html_normal += `
                <li><span>${i}위</span><span>${nickname}</span><span>${points}</span></li>
            `;
        }

        for (let i = 1; i <= 10; i++) {
            const user = challenge_data.data[i - 1];
            const nickname = user ? user.nickname : "";
            const points = user ? user.points : "";

            html_challenge += `
                <li><span>${i}위</span><span>${nickname}</span><span>${points}</span></li>
            `;
        }

        rankingListNormal.innerHTML = html_normal;
        rankingListChallenge.innerHTML = html_challenge;
    }
}