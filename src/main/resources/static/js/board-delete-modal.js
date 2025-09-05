// 게시글 삭제 모달 JS
(function () {
  const menu = document.getElementById("postMenu");
  const deleteBtnInMenu = menu
    ? menu.querySelector('[data-action="delete"]')
    : null;

  const modal = document.getElementById("confirmDelete");
  const confirmBtn = document.getElementById("confirmDeleteBtn");
  const backdrop = modal ? modal.querySelector(".post-modal__backdrop") : null;
  let lastFocused = null;
  let deleteUrl = null;

  if (!deleteBtnInMenu || !modal || !confirmBtn || !backdrop) return;

  function openModal(url) {
    lastFocused = document.activeElement;
    deleteUrl = url || deleteBtnInMenu.getAttribute("data-delete-url");
    modal.hidden = false;
    document.body.style.overflow = "hidden";
    confirmBtn.focus();
    document.addEventListener("keydown", onKeydown);
  }
  function closeModal() {
    modal.hidden = true;
    document.body.style.overflow = "";
    document.removeEventListener("keydown", onKeydown);
    if (lastFocused) lastFocused.focus();
  }
  function onKeydown(e) {
    if (e.key === "Escape") closeModal();
  }

  // 삭제 메뉴 클릭 → 모달 오픈
  deleteBtnInMenu.addEventListener("click", function () {
    // 드롭다운 닫고 모달 열기 (드롭다운을 post-menu.js로 닫는 중이라면 약간 지연)
    setTimeout(() => openModal(this.getAttribute("data-delete-url")), 0);
  });

  // 취소: backdrop 또는 data-close 버튼
  modal.addEventListener("click", function (e) {
    if (e.target.matches('[data-close="true"]')) closeModal();
  });

  // 확인: URL 있으면 이동 / 없으면 커스텀 이벤트 발행
  confirmBtn.addEventListener("click", async function () {
    if (deleteUrl) {
      // ① 서버로 이동 (GET/POST 방식은 서버 설계에 맞게)
      window.location.href = deleteUrl;
    } else {
      // ② fetch 사용시 예시 (postId 사용):
      // await fetch(`/api/posts/${postId}`, { method: 'DELETE' });
      // location.replace('/'); // 완료 후 이동
      document.dispatchEvent(new CustomEvent("post:delete:confirm"));
    }
  });
})();
