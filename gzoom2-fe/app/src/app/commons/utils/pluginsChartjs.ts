import { Plugin } from "chart.js";

/**
 * Increase Spacing Between Legend and Chart
 */
export const legendMargin = {
    id: 'legendMargin',
    beforeInit(chart) {
        // Get a reference to the original fit function
        const origFit = chart.legend.fit;
        chart.legend.fit = function fit() {
            origFit.bind(chart.legend)();
            // Change the height to any desired value
            this.height += 20;
        }
    }
}

export const boldLabelsOnHover: Plugin = {
    id: 'boldLabelsOnHover',

} 