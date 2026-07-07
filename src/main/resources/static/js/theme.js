document.addEventListener('DOMContentLoaded', () => {
    const themeToggleBtn = document.getElementById('theme-toggle');
    const themeIcon = document.getElementById('theme-icon');
    
    // Check local storage or system preference
    const storedTheme = localStorage.getItem('theme');
    const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    
    let currentTheme = storedTheme || (systemPrefersDark ? 'dark' : 'light');
    
    // Apply initial theme
    document.documentElement.setAttribute('data-theme', currentTheme);
    updateIcon(currentTheme);

    // Toggle theme on click
    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', () => {
            currentTheme = currentTheme === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', currentTheme);
            localStorage.setItem('theme', currentTheme);
            updateIcon(currentTheme);
        });
    }

    function updateIcon(theme) {
        if (!themeIcon) return;
        
        if (theme === 'dark') {
            // Moon icon for dark theme (to switch to light)
            themeIcon.innerHTML = '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />';
        } else {
            // Sun icon for light theme (to switch to dark)
            themeIcon.innerHTML = '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />';
        }
    }

    // Set active class on navbar links
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath || (href !== '/' && currentPath.startsWith(href))) {
            link.classList.add('active');
        }
    });

    // Profile Dropdown Logic
    const profileBtn = document.getElementById('profile-menu-btn');
    const profileDropdown = document.getElementById('profile-dropdown');

    if (profileBtn && profileDropdown) {
        // Toggle dropdown on button click
        profileBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            const isExpanded = profileBtn.getAttribute('aria-expanded') === 'true';
            
            if (isExpanded) {
                closeDropdown();
            } else {
                openDropdown();
            }
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', (e) => {
            if (!profileDropdown.contains(e.target) && !profileBtn.contains(e.target)) {
                closeDropdown();
            }
        });

        // Close dropdown on Escape key
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                closeDropdown();
            }
        });

        function openDropdown() {
            profileDropdown.style.opacity = '1';
            profileDropdown.style.visibility = 'visible';
            profileDropdown.style.transform = 'translateY(0)';
            profileBtn.setAttribute('aria-expanded', 'true');
            // Subtle pop effect on avatar
            profileBtn.style.transform = 'scale(1.05)';
        }

        function closeDropdown() {
            profileDropdown.style.opacity = '0';
            profileDropdown.style.visibility = 'hidden';
            profileDropdown.style.transform = 'translateY(-10px)';
            profileBtn.setAttribute('aria-expanded', 'false');
            profileBtn.style.transform = 'scale(1)';
        }
        
        // Add dynamic hover styles for dropdown items via JS to avoid polluting global CSS if not needed
        const dropdownItems = document.querySelectorAll('.dropdown-item');
        dropdownItems.forEach(item => {
            item.addEventListener('mouseenter', () => {
                if (item.tagName === 'BUTTON') {
                    item.style.background = 'rgba(239, 68, 68, 0.1)';
                } else {
                    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
                    item.style.background = isDark ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0.05)';
                    item.style.color = 'var(--text-primary)';
                }
            });
            item.addEventListener('mouseleave', () => {
                item.style.background = 'none';
                if (item.tagName !== 'BUTTON') {
                    item.style.color = 'var(--text-secondary)';
                }
            });
        });
    }
});

