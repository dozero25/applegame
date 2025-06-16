window.onload = () => {
    HeaderService.getInstance().loadHeader();
    RegisterService.getInstance().saveUserInfo();
    const compEvent = ComponentEvent.getInstance();
    compEvent.reactLoginAndRegister();
}


class ComponentEvent {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new ComponentEvent();
        }
        return this.#instance;
    }

    reactLoginAndRegister() {
        // 탭 클릭 이벤트
        document.querySelectorAll('.tabs h3 a').forEach(tab => {
            tab.addEventListener('click', function (event) {
                event.preventDefault();
                document.querySelectorAll('.tabs h3 a').forEach(t => t.classList.remove('active'));
                this.classList.add('active');

                const tabContent = document.querySelector(this.getAttribute('href'));
                document.querySelectorAll('div[id$="tab-content"]').forEach(c => c.classList.remove('active'));
                if (tabContent) tabContent.classList.add('active');
            });
        });

        // 슬라이드쇼
        const slideshow = document.getElementById('slideshow');
        if (slideshow) {
            const slides = slideshow.children;
            let currentSlide = 0;

            for (let i = 1; i < slides.length; i++) {
                slides[i].style.display = 'none';
            }

            setInterval(() => {
                slides[currentSlide].style.display = 'none';
                currentSlide = (currentSlide + 1) % slides.length;
                slides[currentSlide].style.display = 'block';
            }, 3850);
        }

        // 클래스 스왑 함수
        function swapClass(el, removeClass, addClass) {
            if (!el) return; // 요소가 없으면 그냥 종료
            el.classList.remove(removeClass);
            el.classList.add(addClass);
        }

        // 패널 show/hide
        document.querySelectorAll('.agree, .forgot, #toggle-terms, .log-in, .sign-up').forEach(trigger => {
            trigger.addEventListener('click', function (event) {
                event.preventDefault();

                const user = document.querySelector('.user');
                const terms = document.querySelector('.terms');
                const form = document.querySelector('.form-wrap');
                const recovery = document.querySelector('.recovery');
                const close = document.getElementById('toggle-terms');

                const isTermsOpen = terms?.classList.contains('open');
                const isRecoveryOpen = recovery?.classList.contains('open');

                if (this.classList.contains('agree') || this.classList.contains('log-in') || (this.id === 'toggle-terms' && isTermsOpen)) {
                    if (isTermsOpen) {
                        swapClass(form, 'open', 'closed');
                        swapClass(terms, 'open', 'closed');
                        swapClass(close, 'open', 'closed');
                    } else {
                        if (this.classList.contains('log-in')) return;
                        swapClass(form, 'closed', 'open');
                        swapClass(terms, 'closed', 'open');
                        if (terms) terms.scrollTop = 0;
                        swapClass(close, 'closed', 'open');
                        if (user) user.classList.add('overflow-hidden');
                    }
                } else if (this.classList.contains('forgot') || this.classList.contains('sign-up') || this.id === 'toggle-terms') {
                    if (isRecoveryOpen) {
                        swapClass(form, 'open', 'closed');
                        swapClass(recovery, 'open', 'closed');
                        swapClass(close, 'open', 'closed');
                    } else {
                        if (this.classList.contains('sign-up')) return;
                        swapClass(form, 'closed', 'open');
                        swapClass(recovery, 'closed', 'open');
                        swapClass(close, 'closed', 'open');
                        if (user) user.classList.add('overflow-hidden');
                    }
                }
            });
        });

        // 메시지 표시
        const recoveryButton = document.querySelector('.recovery .button');
        if (recoveryButton) {
            recoveryButton.addEventListener('click', function (event) {
                event.preventDefault();
                const msg = document.querySelector('.recovery .mssg');
                if (msg) msg.classList.add('animate');

                setTimeout(() => {
                    const form = document.querySelector('.form-wrap');
                    const recovery = document.querySelector('.recovery');
                    const close = document.getElementById('toggle-terms');
                    const arrow = document.querySelector('.tabs-content .fa');

                    swapClass(form, 'open', 'closed');
                    swapClass(recovery, 'open', 'closed');
                    swapClass(close, 'open', 'closed');
                    if (arrow) swapClass(arrow, 'active', 'inactive');
                    if (msg) msg.classList.remove('animate');
                }, 2500);
            });
        }
    }

}