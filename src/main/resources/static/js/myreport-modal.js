/* =========================================================
   Reports Page JS (rpts-)  —  /assets/js/reports.js
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
  const tbody = document.getElementById("rptsTbody");

  // 상세 모달
  const detailModal = document.getElementById("rptsDetailModal");
  const titleEl = document.getElementById("rptsTitle"); // readOnly
  const reasonEl = document.getElementById("rptsReason");
  const contentEl = document.getElementById("rptsContent");
  const saveBtn = document.getElementById("rptsSaveBtn");
  const deleteBtn = document.getElementById("rptsDeleteBtn");
  const actionsRight = detailModal.querySelector(".rpts-actions__right"); // 삭제/수정 묶음

  // 삭제 확인 모달
  const confirmModal = document.getElementById("rptsConfirmModal");
  const confirmBtn = document.getElementById("rptsConfirmDeleteBtn");

  // 상태
  const dataEl = document.getElementById("reports-data");
  let reports = JSON.parse(dataEl.dataset.json);
  console.log(reports);

  let currentId = null;
  let original = null; // { reason, content }
  let detailLocked = false; // 처리 완료 잠금 플래그

  /* ---------- reason 매핑 ---------- */
  const REASON_MAP = {
    INAPPROPRIATE_AUTHOR_NAME: "부적절한 작성자명",
    INAPPROPRIATE_BOARD_CONTENT: "부적절한 글 내용",
    ADVERTISING_BOARD_CONTENT: "광고성 글 내용",
    COPYRIGHT_VIOLATION: "저작권 침해",
    PERSONAL_INFORMATION: "개인정보 노출",
    ABUSIVE_LANGUAGE: "욕설/비방",
    SPAM: "도배/스팸",
    ETC: "기타",
  };

  function pickReasonText(r) {
    const raw = r.result ?? "";
    return REASON_MAP[raw] ?? raw;
  }

  function setReasonBack(r, newReasonText) {
    const code =
        Object.entries(REASON_MAP).find(
            ([, label]) => label === newReasonText
        )?.[0] ?? newReasonText;

    r.result = code; // 핵심 필드 하나만 갱신
  }

  /* ---------- 렌더링 ---------- */
  const statusClass = (s) =>
    s === "COMPLETED"
      ? "rpts-status rpts-status--done"
      : s === "PROCESSING"
      ? "rpts-status rpts-status--prog"
      : "rpts-status rpts-status--wait";

  function render() {
    tbody.innerHTML = reports
      .map((r) => {
        const reasonTxt = pickReasonText(r);
        return `
          <tr data-id="${r.id}" tabindex="0">
            <td>${r.id}</td>
            <td title="${escapeHtml(r.boardTitle  ?? "")}">${escapeHtml(
          r.boardTitle ?? ""
        )}</td>
            <td title="${escapeHtml(reasonTxt)}">${escapeHtml(
          reasonTxt || "-"
        )}</td>
            <td title="${escapeHtml(r.content ?? "")}">${escapeHtml(
          r.content ?? ""
        )}</td>
            <td><span class="${statusClass(
              r.status ?? "대기 중"
            )}">${escapeHtml(r.status ?? "대기 중")}</span></td>
          </tr>
        `;
      })
      .join("");
  }
  render();

  /* ---------- 행 클릭/키보드로 상세 열기 ---------- */
  tbody.addEventListener("click", openFromEvent);
  tbody.addEventListener("keydown", (e) => {
    if (e.key === "Enter" || e.key === " ") {
      e.preventDefault();
      openFromEvent(e);
    }
  });

  function openFromEvent(e) {
    const tr = e.target.closest("tr");
    if (!tr) return;
    openDetail(Number(tr.dataset.id));
  }

  /* ---------- 상세 모달 ---------- */
  function openDetail(id) {
    const item = reports.find((r) => r.id === id);
    if (!item) return;

    currentId = id;
    titleEl.value = item.boardTitle ?? ""; // readOnly

    const reasonTxt = pickReasonText(item);
    ensureOption(reasonEl, reasonTxt || "기타");
    reasonEl.value = reasonTxt || "기타";

    contentEl.value = (item.content ?? "").toString();

    original = { reason: reasonEl.value, content: contentEl.value };
    saveBtn.disabled = true;

    // ── 완료 상태 잠금 처리 ──
    detailLocked = item.status === "처리 완료";
    if (detailLocked) {
      // 삭제/수정 영역은 숨기고
      actionsRight.style.display = "none";
      // 입력들 읽기 전용
      reasonEl.disabled = true;
      contentEl.readOnly = true;
    } else {
      // 다른 건 노출 & 활성화
      actionsRight.style.display = "";
      reasonEl.disabled = false;
      contentEl.readOnly = false;
      deleteBtn.disabled = false;
    }

    show(detailModal);
    trapFocus(detailModal);
  }

  // select에 없는 이유 라벨이 오면 임시 옵션을 추가
  function ensureOption(selectEl, value) {
    const exists = Array.from(selectEl.options).some(
      (opt) => opt.value === value
    );
    if (!exists) {
      const opt = document.createElement("option");
      opt.value = value;
      opt.textContent = value;
      selectEl.appendChild(opt);
    }
  }

  // 변경 감지 → 저장 버튼 활성화 (완료 상태면 무시)
  [reasonEl, contentEl].forEach((el) => {
    el.addEventListener("input", () => {
      if (detailLocked) return; // 잠금 중엔 무시
      const changed =
        reasonEl.value !== original.reason ||
        contentEl.value.trim() !== original.content.trim();
      saveBtn.disabled = !changed;
    });
  });

  // 닫기 버튼들 및 백드롭 클릭
  document.querySelectorAll('[data-close="true"]').forEach((btn) => {
    btn.addEventListener("click", () => {
      hide(detailModal);
      hide(confirmModal);
    });
  });
  detailModal.addEventListener("click", (e) => {
    if (e.target.classList.contains("rpts-modal__backdrop")) hide(detailModal);
  });
  confirmModal.addEventListener("click", (e) => {
    if (e.target.classList.contains("rpts-modal__backdrop")) hide(confirmModal);
  });

  // ESC로 닫기
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      if (!confirmModal.classList.contains("is-hidden")) hide(confirmModal);
      else if (!detailModal.classList.contains("is-hidden")) hide(detailModal);
    }
  });

  // 저장(수정)
  document.getElementById("rptsDetailForm").addEventListener("submit", (e) => {
    e.preventDefault();
    if (detailLocked || saveBtn.disabled || currentId == null) return;

    const idx = reports.findIndex((r) => r.id === currentId);
    if (idx >= 0) {
      const newReason = reasonEl.value;
      const newContent = contentEl.value.trim();

      reports[idx].result = newReason;
      reports[idx].content = newContent;
      render();
    }
    hide(detailModal);
  });

  // 삭제 → 확인 모달 (완료 상태면 열지 않음)
  deleteBtn.addEventListener("click", () => {
    if (currentId == null || detailLocked) return;
    show(confirmModal);
    trapFocus(confirmModal);
  });

  // 확인 모달에서 삭제 확정
  confirmBtn.addEventListener("click", () => {
    if (currentId == null) return;
    reports = reports.filter((r) => r.id !== currentId);
    render();
    hide(confirmModal);
    hide(detailModal);
  });

  /* ---------- helpers ---------- */
  function show(modal) {
    modal.classList.remove("is-hidden");
  }
  function hide(modal) {
    modal.classList.add("is-hidden");
  }

  function escapeHtml(s) {
    return String(s)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#39;");
  }

  // 간단 포커스 트랩
  function trapFocus(modal) {
    const focusable = modal.querySelectorAll(
      'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
    );
    if (!focusable.length) return;

    const first = focusable[0];
    const last = focusable[focusable.length - 1];
    setTimeout(() => first.focus(), 0);

    function handle(e) {
      if (e.key !== "Tab") return;
      if (e.shiftKey && document.activeElement === first) {
        e.preventDefault();
        last.focus();
      } else if (!e.shiftKey && document.activeElement === last) {
        e.preventDefault();
        first.focus();
      }
    }
    modal.addEventListener("keydown", handle, { once: true });
  }
});
