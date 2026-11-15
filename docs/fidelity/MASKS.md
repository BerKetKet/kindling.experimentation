# Fixture Masks Configuration

Format:
`<id>: <x0>,<y0>,<x1>,<y1> [<x0>,<y0>,<x1>,<y1> ...]`

# D01: Library grid (Mask 17px strip where bottom row covers are cut off by nav bar at y=2094..2111)
D01: 0,2094,1080,2111

# D04: Settings (Mask device name and email address per privacy rule)
D04: 45,355,650,395 45,1190,680,1235

# D10: Reader Chrome (Mask reading text area to compare chrome bars)
D10: 0,251,1080,2040

# D12: In-book search (Mask virtual software keyboard)
D12: 0,1290,1080,2340

# D16: Annotations share sheet (Mask name and email address per privacy rule)
D16: 40,1675,1040,1715 40,1980,620,2060

# R03-R06: Reading Canvas (Mask reading text area to compare page background, margins, and header/footer only)
R03: 50,180,1030,2200
R04: 50,180,1030,2200
R05: 50,180,1030,2200
R06: 50,180,1030,2200
