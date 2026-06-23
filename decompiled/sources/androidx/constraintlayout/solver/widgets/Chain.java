package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;

/* JADX INFO: loaded from: classes.dex */
class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        ChainHead[] chainHeadArr;
        int i3;
        if (i == 0) {
            i2 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = 0;
        } else {
            i2 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
            i3 = 2;
        }
        for (int i4 = 0; i4 < i2; i4++) {
            ChainHead chainHead = chainHeadArr[i4];
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i3, chainHead)) {
                    applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
                }
            } else {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:269:0x04cb  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x04d0  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x04d6  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x04db  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x04df  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x04f1  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x004a A[PHI: r13 r14
      0x004a: PHI (r13v4 boolean) = (r13v2 boolean), (r13v32 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]
      0x004a: PHI (r14v4 boolean) = (r14v2 boolean), (r14v33 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x004c A[PHI: r13 r14
      0x004c: PHI (r13v29 boolean) = (r13v2 boolean), (r13v32 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]
      0x004c: PHI (r14v30 boolean) = (r14v2 boolean), (r14v33 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0158  */
    /* JADX WARN: Type inference failed for: r0v30, types: [androidx.constraintlayout.solver.LinearSystem] */
    /* JADX WARN: Type inference failed for: r0v31 */
    /* JADX WARN: Type inference failed for: r0v42 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6, types: [androidx.constraintlayout.solver.LinearSystem] */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r5v29 */
    /* JADX WARN: Type inference failed for: r5v30 */
    /* JADX WARN: Type inference failed for: r5v31, types: [androidx.constraintlayout.solver.SolverVariable] */
    /* JADX WARN: Type inference failed for: r5v34 */
    /* JADX WARN: Type inference failed for: r5v91 */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v2, types: [androidx.constraintlayout.solver.widgets.ConstraintWidget] */
    /* JADX WARN: Type inference failed for: r7v26 */
    /* JADX WARN: Type inference failed for: r7v27 */
    /* JADX WARN: Type inference failed for: r7v28 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static void applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r34, androidx.constraintlayout.solver.LinearSystem r35, int r36, int r37, androidx.constraintlayout.solver.widgets.ChainHead r38) {
        /*
            Method dump skipped, instruction units count: 1306
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.Chain.applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer, androidx.constraintlayout.solver.LinearSystem, int, int, androidx.constraintlayout.solver.widgets.ChainHead):void");
    }
}
