document.addEventListener('DOMContentLoaded', function(){
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
})