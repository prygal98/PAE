function relativeTime(time) {
    const units = {
        year: 24 * 60 * 60 * 1000 * 365,
        month: (24 * 60 * 60 * 1000 * 365) / 12,
        day: 24 * 60 * 60 * 1000,
        hour: 60 * 60 * 1000,
        minute: 60 * 1000,
        second: 1000,
    };

    const rtf = new Intl.RelativeTimeFormat('fr', { numeric: 'auto' });

    const elapsed = new Date(time) - new Date();
    // eslint-disable-next-line no-restricted-syntax
    for (const u in units) {
        if (Math.abs(elapsed) > units[u] || u === 'second')
            return rtf.format(Math.round(elapsed / units[u]), u);
    }

    return '';
}

export default relativeTime;