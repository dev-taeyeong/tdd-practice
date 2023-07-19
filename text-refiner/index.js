function refineText(source, options) {
    return [
        normalizeWhiteSpaces,
        compactWhiteSpaces,
        maskBannedWords,
        trimWhitespaces
    ].reduce(
        (value, cb) => cb(value, options),
        source
    );
}

function normalizeWhiteSpaces(source) {
    return source.replace('\t', ' ');
}

function compactWhiteSpaces(source) {
    return source.indexOf('  ') < 0 
    ? source 
    : compactWhiteSpaces(source.replace('  ', ' '));
}

function maskBannedWords(source, options) {
    return options 
        ? options.bannedWords.reduce(maskBannedWord, source)
        : source;
}

function maskBannedWord(source, bannedWord) {
    const mask = '*'.repeat(bannedWord.length);
    return source.replace(bannedWord, mask);
}

function trimWhitespaces(value) {
    return value.trim();
}

module.exports = refineText;
