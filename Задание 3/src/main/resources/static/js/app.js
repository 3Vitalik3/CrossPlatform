const menuButton = document.querySelector('.menu-toggle');
const navigation = document.querySelector('.main-nav');

menuButton?.addEventListener('click', () => {
    const isOpen = menuButton.getAttribute('aria-expanded') === 'true';
    menuButton.setAttribute('aria-expanded', String(!isOpen));
    navigation.classList.toggle('open', !isOpen);
});

navigation?.querySelectorAll('a').forEach(link => link.addEventListener('click', () => {
    menuButton?.setAttribute('aria-expanded', 'false');
    navigation.classList.remove('open');
}));

document.querySelectorAll('[data-filter]').forEach(button => {
    button.addEventListener('click', () => {
        const selected = button.dataset.filter;
        document.querySelectorAll('[data-filter]').forEach(item => item.classList.toggle('active', item === button));
        document.querySelectorAll('[data-category]').forEach(card => {
            card.hidden = selected !== 'all' && card.dataset.category !== selected;
        });
    });
});
