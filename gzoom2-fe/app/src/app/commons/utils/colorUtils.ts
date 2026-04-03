import { ChartDataset } from "chart.js";

export function selectColor(opacity: number): string {
    let o = Math.round, r = Math.random, s = 255;
    return 'rgba(' + o(r() * s) + ',' + o(r() * s) + ',' + o(r() * s) + ',' + opacity + ')';
}

export function setOpacityByRGBA(rgbaString: string, opacity: number): string {
    // Extract the individual RGBA components
    const matches = rgbaString.match(/^rgba\((\d+),\s*(\d+),\s*(\d+),\s*(\d*\.?\d+)\)$/);
    if (!matches) {
        throw new Error('Invalid RGBA string');
    }

    const red = parseInt(matches[1], 10);
    const green = parseInt(matches[2], 10);
    const blue = parseInt(matches[3], 10);
    const alpha = parseFloat(matches[4]);

    // Clamp the opacity value between 0 and 1
    const clampedOpacity = Math.min(Math.max(opacity, 0), 1);

    // Create the modified RGBA string with the updated opacity
    const modifiedRGBA = `rgba(${red}, ${green}, ${blue}, ${clampedOpacity})`;
    return modifiedRGBA;
}

export function setOpacityByHex(hexString: string, opacity: number): string {
    // Remove the hash at the start if it's there
    if (hexString.startsWith('#')) {
        hexString = hexString.slice(1);
    }

    // Parse the hex string into RGB components
    let red, green, blue;

    if (hexString.length === 3) {
        // Shorthand hex color (#RGB)
        red = parseInt(hexString[0] + hexString[0], 16);
        green = parseInt(hexString[1] + hexString[1], 16);
        blue = parseInt(hexString[2] + hexString[2], 16);
    } else if (hexString.length === 6) {
        // Full hex color (#RRGGBB)
        red = parseInt(hexString.slice(0, 2), 16);
        green = parseInt(hexString.slice(2, 4), 16);
        blue = parseInt(hexString.slice(4, 6), 16);
    } else {
        throw new Error('Invalid hex color string');
    }

    // Clamp the opacity value between 0 and 1
    const clampedOpacity = Math.min(Math.max(opacity, 0), 1);

    // Create the modified RGBA string with the updated opacity
    const modifiedRGBA = `rgba(${red}, ${green}, ${blue}, ${clampedOpacity})`;
    return modifiedRGBA;
}

export const gzoomColorsChart = [
    '#4E79A7',
    '#A0CBE8',
    '#F28E2B',
    '#FFBE7D',
    '#59A14F',
    '#8CD17D',
    '#B6992D',
    '#F1CE63',
    '#499894',
    '#86BCB6',
    '#E15759',
    '#FF9D9A',
    '#79706E',
    '#BAB0AC',
    '#D37295',
    '#FABFD2',
    '#B07AA1',
    '#D4A6C8',
    '#9D7660',
    '#D7B5A6',
    '#8A2BE2',
    '#FF6347',
    '#3CB371',
    '#FFD700',
    '#6A5ACD',
    '#FF4500',
    '#2E8B57',
    '#DA70D6',
    '#4682B4',
    '#FF1493'
];

export function getColorFromGzoomColorChart(index: number): string {
    const length = gzoomColorsChart.length;
    const cyclicIndex = index % length;
    return gzoomColorsChart[cyclicIndex];
}


export function setDatasetsColor(value: ChartDataset[], opacityBackgroundColor?: number): ChartDataset[] {
    if (value) {
        value.forEach((x, index) => {
            const color = getColorFromGzoomColorChart(index)
            x.borderColor = color;
            x.backgroundColor = setOpacityByHex(color, opacityBackgroundColor ?? 0.5);
        })
        return value;
    }
    return null;
}
