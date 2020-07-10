const k8s = require('@kubernetes/client-node');

const kc = new k8s.KubeConfig();
kc.loadFromFile("/home/emilien/.kube/config"); // change the location to your kubeconfig file

const k8sApi = kc.makeApiClient(k8s.CoreV1Api);

(async () => {
    try {
        // fetch pods with the label app and as a value: ranked
        // documentation : https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/
        const rankedLabel = "app=ranked";
        const rankedPodsInMinecraftNamespace = (await k8sApi.listNamespacedPod('minecraft', undefined, undefined, undefined, undefined, rankedLabel)).body;

        // this tell to k8s to merge the new changes with the current changes
        const headers = { 'content-type': 'application/merge-patch+json' };
        // modify the label "occupied"
        const bodyPatch = { "metadata": { "labels": { "occupied": "true" } } };

        // loop over the pods but the method patchNamespacedPod can be used separately for editing a specific pod
        rankedPodsInMinecraftNamespace.items.forEach(pod => {
            const podName = pod.metadata.name;
            k8sApi.patchNamespacedPod(podName, "minecraft", bodyPatch, undefined, undefined, undefined, undefined, { headers }).body;
        });
    } catch (error) {
        console.log(error.body);
    }
})();