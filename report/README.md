# Report — Building Accessible Android Applications

Source for the group report (English, LaTeX).

## Compile

Use **XeLaTeX** (needed for the Vietnamese names and the Times New Roman / Consolas fonts).
Run twice so the table of contents and cross-references resolve:

```bash
xelatex main.tex
xelatex main.tex
```

Output: `main.pdf` (≈18 pages).

## Structure

```
main.tex               preamble, fonts, Kotlin listing style, section includes
sections/
  00_title.tex         title page + member/role table
  01_introduction.tex  Introduction (Phát)
  02_background.tex     Background: EAA/WCAG, POUR, Composition vs Semantics tree (Phát)
  03_casestudy.tex      App overview, architecture, technique→file map (Tính)
  04_core_semantics.tex Core semantics (Phong)
  05_advanced_semantics.tex  Advanced semantics & navigation (Khương)
  06_testing_evaluation.tex  Testing, Scanner case study, metrics, trade-offs (Tính)
  07_conclusion.tex     Conclusion + AI usage
  08_appendix.tex       Task division, run instructions, limitations
  09_references.tex     Bibliography
images/
  home.png detail.png player.png   app screens
  merge.png fontscale.png           accessibility figures
  scanner_agoda.png                 real Accessibility Scanner evidence
  mockup_sources/                   HTML used to render the app figures
```

## Regenerating the app figures

The three app screens and the two accessibility figures are HTML mockups (faithful to the Compose
code) rendered with headless Chrome. To rebuild after editing `images/mockup_sources/*.html`:

```bash
CHROME="/c/Program Files/Google/Chrome/Application/chrome.exe"
"$CHROME" --headless=new --force-device-scale-factor=2 --window-size=470,920 \
  --screenshot="<abs>/images/home.png" "file:///<abs>/images/mockup_sources/mock_home.html"
```

> The mockups stand in for emulator screenshots so the report is reproducible without an Android
> device. Replace them with real TalkBack/emulator captures for the final submission if you wish.
