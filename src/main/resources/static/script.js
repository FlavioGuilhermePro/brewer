// Aguarda o carregamento completo do DOM
document.addEventListener('DOMContentLoaded', function() {

    // ===== FUNCIONALIDADE DE DELETAR PRODUTOS =====
    const deleteButtons = document.querySelectorAll('.js-delete-btn');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            const confirmed = confirm('Tem certeza que deseja deletar este produto?');
            if (confirmed) {
                event.target.closest('form').submit();
            }
        });
    });

    // ===== FUNCIONALIDADE DO MENU RESPONSIVO =====
    const menuToggle = document.getElementById('menu-toggle');
    const sidebar = document.querySelector('.sidebar');

    // Verifica se os elementos existem antes de continuar
    if (!menuToggle || !sidebar) {
        console.warn('Menu toggle ou sidebar não encontrados');
        return;
    }

    // Cria overlay para fechar o menu ao clicar fora
    const overlay = document.createElement('div');
    overlay.className = 'sidebar-overlay';
    document.body.appendChild(overlay);

    // Função para abrir o menu
    function openMenu() {
        sidebar.classList.add('sidebar-active');
        overlay.classList.add('active');
        document.body.style.overflow = 'hidden'; // Previne scroll do body
    }

    // Função para fechar o menu
    function closeMenu() {
        sidebar.classList.remove('sidebar-active');
        overlay.classList.remove('active');
        document.body.style.overflow = ''; // Restaura scroll
    }

    // Toggle do menu ao clicar no botão
    menuToggle.addEventListener('click', function(e) {
        e.stopPropagation();
        if (sidebar.classList.contains('sidebar-active')) {
            closeMenu();
        } else {
            openMenu();
        }
    });

    // Fecha o menu ao clicar no overlay
    overlay.addEventListener('click', closeMenu);

    // Fecha o menu ao clicar em um link do menu
    const sidebarLinks = sidebar.querySelectorAll('a');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function() {
            // Só fecha o menu em telas menores
            if (window.innerWidth <= 768) {
                closeMenu();
            }
        });
    });

    // Fecha o menu ao pressionar a tecla ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && sidebar.classList.contains('sidebar-active')) {
            closeMenu();
        }
    });

    // Fecha o menu ao redimensionar a tela para desktop
    window.addEventListener('resize', function() {
        if (window.innerWidth > 768 && sidebar.classList.contains('sidebar-active')) {
            closeMenu();
        }
    });
});