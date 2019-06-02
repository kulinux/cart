import { useEffect, useState } from 'react';
import { Stream } from 'xstream';

const useStream = <T>(stream$: Stream<T>, initialState: T | null = null) => {
    const [current, setCurrent] = useState<T>(initialState);

    useEffect(() => {
        const sub = stream$.subscribe({
            next: setCurrent,
        });
        return () => sub.unsubscribe();
    });
    // Just return our current value, since that's the thing we're interested in
    // (to render) when using this hook:
    return current;
};
export default useStream;