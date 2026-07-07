// Give the nav a solid background once you scroll past the hero.
const navigation = document.getElementById('navigation');

window.addEventListener('scroll', function () {
  if (window.scrollY > 60) {
    navigation.classList.add('scrolled');
  } else {
    navigation.classList.remove('scrolled');
  }
});